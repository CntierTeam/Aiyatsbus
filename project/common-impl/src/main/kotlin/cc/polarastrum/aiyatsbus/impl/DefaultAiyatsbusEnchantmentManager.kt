package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.*
import cc.polarastrum.aiyatsbus.core.compat.EnchantRegistrationHooks
import cc.polarastrum.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import cc.polarastrum.aiyatsbus.core.util.FileWatcher.isProcessingByWatcher
import cc.polarastrum.aiyatsbus.core.util.FileWatcher.unwatch
import cc.polarastrum.aiyatsbus.core.util.FileWatcher.watch
import cc.polarastrum.aiyatsbus.core.util.YamlUpdater
import cc.polarastrum.aiyatsbus.core.util.deepRead
import cc.polarastrum.aiyatsbus.core.util.reloadable
import cc.polarastrum.aiyatsbus.core.util.safeguard
import cc.polarastrum.aiyatsbus.impl.DefaultAiyatsbusAPI.Companion.proxy
import cc.polarastrum.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
import org.bukkit.NamespacedKey
import taboolib.common.LifeCycle
import taboolib.common.TabooLib
import taboolib.common.io.newFolder
import taboolib.common.io.runningResourcesInJar
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common.util.replaceWithOrder
import taboolib.common.util.t
import taboolib.module.configuration.Configuration
import taboolib.module.nms.MinecraftVersion.versionId
import taboolib.platform.util.onlinePlayers
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

class DefaultAiyatsbusEnchantmentManager : AiyatsbusEnchantmentManager {

    private val byKeyMap = ConcurrentHashMap<NamespacedKey, AiyatsbusEnchantment>()
    private val byKeyStringMap = ConcurrentHashMap<String, AiyatsbusEnchantment>()
    private val byNameMap = ConcurrentHashMap<String, AiyatsbusEnchantment>()
    private val enchantmentsToRegister = CopyOnWriteArraySet<AiyatsbusEnchantmentBase>()

    override fun getEnchants(): Map<NamespacedKey, AiyatsbusEnchantment> {
        return byKeyMap
    }

    override fun getEnchant(key: NamespacedKey): AiyatsbusEnchantment? {
        return byKeyMap[key]
    }

    override fun getEnchant(key: String): AiyatsbusEnchantment? {
        return byKeyStringMap[key]
    }

    override fun getByName(name: String): AiyatsbusEnchantment? {
        return byNameMap[name]
    }

    override fun register(enchantment: AiyatsbusEnchantmentBase) {
        if (enchantment !is InternalAiyatsbusEnchantmentBase) {
            enchantmentsToRegister += enchantment
        }
        if (TabooLib.getCurrentLifeCycle() != LifeCycle.LOAD) {
            val ench = Aiyatsbus.api().getEnchantmentRegisterer().register(enchantment) as AiyatsbusEnchantment
            byKeyMap[enchantment.enchantmentKey] = ench
            byKeyStringMap[enchantment.basicData.id] = ench
            byNameMap[enchantment.basicData.originName] = ench
        }
        EnchantRegistrationHooks.registerHooks()
    }

    override fun unregister(enchantment: AiyatsbusEnchantment) {
        enchantment.mechanism?.close()
        enchantmentsToRegister.remove(enchantment)
        Aiyatsbus.api().getEnchantmentRegisterer().unregister(enchantment)
        byKeyMap -= enchantment.enchantmentKey
        byKeyStringMap -= enchantment.basicData.id
        byNameMap -= enchantment.basicData.originName
    }

    override fun loadEnchantments() {
        val enchantmentCount = byKeyMap.size
        clearEnchantments()

        val enchantsFolder = newFolder(getDataFolder(), "enchants")
        if (enchantsFolder.listFiles()?.none { it.isDirectory } == true && AiyatsbusSettings.autoReleaseEnchants) {
            releaseDefaultEnchants()
        }

        val startTime = System.currentTimeMillis()
        enchantsFolder.listFiles { file, _ -> file.isDirectory }?.toList()?.let { directories ->
            directories.flatMap { it.deepRead("yml") }.forEach(::loadFromFile)
        }

        enchantmentsToRegister.forEach(::register)
        console().sendLang("loading-enchantments", byKeyMap.size, System.currentTimeMillis() - startTime)

        if (enchantmentCount < byKeyMap.size) {
            onlinePlayers.forEach {
                Aiyatsbus.api().getMinecraftAPI().getPacketHandler().synchronizeRegistries(it)
            }
        }
    }

    override fun loadFromFile(file: File) {
        val relativePath = file.path.substring(file.path.indexOf("enchants" + File.separator), file.path.length)
        val config = YamlUpdater.loadFromFile(relativePath, AiyatsbusSettings.enableUpdater, AiyatsbusSettings.updateContents)
        val id = config["basic.id"].toString()
        val key = NamespacedKey.minecraft(id)

        val enchant = loadConfiguredEnchantment(id, file, config)
        if (!enchant.dependencies.checkAvailable()) return

        register(enchant)
        enchant.mechanism?.init()
        setupFileWatcher(file, relativePath, key, id)
    }

    private fun releaseDefaultEnchants() {
        runningResourcesInJar.keys
            .filter { path ->
                path.endsWith(".yml") &&
                    path.startsWith("enchants/") &&
                    path.count { it == '/' } >= 2
            }
            .forEach { releaseResourceFile(it) }
    }

    private fun setupFileWatcher(file: File, relativePath: String, key: NamespacedKey, id: String) {
        file.watch { watchedFile ->
            if (watchedFile.isProcessingByWatcher) {
                watchedFile.isProcessingByWatcher = false
                return@watch
            }

            val startTime = System.currentTimeMillis()
            val resourceStream = javaClass.classLoader.getResourceAsStream(relativePath.replace('\\', '/'))
            if (AiyatsbusSettings.enableUpdater && resourceStream != null) {
                console().sendLang("enchantment-reload-failed", id)
                watchedFile.isProcessingByWatcher = true
                YamlUpdater.loadFromFile(
                    relativePath,
                    AiyatsbusSettings.enableUpdater,
                    AiyatsbusSettings.updateContents,
                    Configuration.loadFromInputStream(resourceStream)
                )
                return@watch
            }

            reloadEnchantment(watchedFile, key, id, startTime)
        }
    }

    private fun reloadEnchantment(file: File, key: NamespacedKey, id: String, startTime: Long) {
        val oldEnchant = getEnchant(key) ?: return
        oldEnchant.mechanism?.close()
        unregister(oldEnchant)

        if (file.exists()) {
            val config = Configuration.loadFromFile(file)
            val newEnchant = loadConfiguredEnchantment(id, file, config)
            if (!newEnchant.dependencies.checkAvailable()) return

            register(newEnchant)
            getEnchant(key)?.mechanism?.init()
        }

        console().sendLang("enchantment-reload", id, System.currentTimeMillis() - startTime)
        EnchantRegistrationHooks.unregisterHooks()
        EnchantRegistrationHooks.registerHooks()
    }

    private fun loadConfiguredEnchantment(id: String, file: File, config: Configuration): InternalAiyatsbusEnchantmentBase {
        val isVanilla = config.getBoolean("alternative.is-vanilla", false) || config.getBoolean("alternative.is_vanilla", false)
        return if (isVanilla) {
            VanillaAiyatsbusEnchantmentBase(id, file, config)
        } else {
            InternalAiyatsbusEnchantmentBase(id, file, config)
        }
    }

    override fun clearEnchantments() {
        for (enchant in byKeyMap.values) {
            if (enchant in enchantmentsToRegister) continue
            enchant.file?.isProcessingByWatcher = false
            enchant.file?.unwatch()
            unregister(enchant)
        }
    }

    companion object {

        private const val PACKAGE = "cc.polarastrum.aiyatsbus.impl.registration.v{0}_nms.DefaultModernEnchantmentRegisterer"

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEnchantmentManager>(DefaultAiyatsbusEnchantmentManager())

            val registerer = when {
                versionId >= 260100 -> modern(260100)
                versionId >= 12104 -> modern(12104)
                versionId >= 12102 -> modern(12103)
                versionId >= 12100 -> modern(12100)
                versionId >= 12005 -> error(
                    """
                    Aiyatsbus does not support Minecraft 1.20.5 or 1.20.6.
                    """.t()
                )
                versionId >= 12003 -> modern(12004)
                else -> DefaultLegacyEnchantmentRegisterer
            }
            DefaultAiyatsbusAPI.registerer = registerer

            if (registerer is ModernEnchantmentRegisterer) {
                safeguard("注册表替换", "registry replacer") { registerer.replaceRegistry() }
                registerLifeCycleTask(LifeCycle.ACTIVE) {
                    registerer.replaceRegistry()
                }
                registerLifeCycleTask(LifeCycle.DISABLE) {
                    Aiyatsbus.api().getEnchantmentManager().clearEnchantments()
                }
            }
            reloadable {
                registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.ENCHANTMENT) {
                    safeguard("附魔", "enchantments") { Aiyatsbus.api().getEnchantmentManager().loadEnchantments() }
                }
            }
        }

        private fun modern(versionId: Int): ModernEnchantmentRegisterer {
            return proxy(PACKAGE.replaceWithOrder(versionId))
        }
    }
}
