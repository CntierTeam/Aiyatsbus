package hamsteryds.nereusopus

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import hamsteryds.nereusopus.enchants.EnchantmentLoader
import hamsteryds.nereusopus.enchants.skill.Xray
import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.api.ParrotXAPI
import hamsteryds.nereusopus.utils.DebugUtils
import hamsteryds.nereusopus.utils.team.TeamDataManagerAPI
import license.LicenseService
import license.PluginProxy
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

class NereusOpus : PPlugin(), PluginProxy {
    init {
        try {
            Class.forName("cc.polarastrum.aiyatsbus.core.AiyatsbusItemStack")
            supportNewAiyatsbus = true
        } catch (_: Throwable) {
        }

        EnchantmentLoader.loadEnchantments(this)
    }

    override fun beforeInit() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
            saveResource("enchantments/artifacts.yml", true)
            saveResource("enchantments/enchants.yml", true)
            saveResource("enchantments/skills.yml", true)
        }
    }

    override fun onLoad() {
        super.onLoad()
        EnchantmentLoader.loadEnchantments()
    }

    override fun preload() {
        lang.log.info("正在启动 NereusOpus")
        pConfig = ConfigReader.getInstance()
        setTimeLog("插件初始化完成, 共耗时&a{0}ms&r.")
    }

    override fun load() {
        DebugUtils.initialize()
        teamDataManager = TeamDataManagerAPI.getInstance()
        teamDataManager!!.load()
    }

    override fun afterInit() {
        registerStats(16162, null)
    }

    override fun onEnable() {
        super.onEnable()
        if (
            EnchantmentLoader.byKey.isEmpty() ||
            Aiyatsbus.api().getEnchantmentManager().getEnchants().size < EnchantmentLoader.byKey.size + 40
        ) {
            lang.log.error("加载", "NereusOpus", "未知原因, 服务器将会被强制关闭")
            lang.log.error("以下是一些可用信息:")
            println(EnchantmentLoader.byKey.toString())
            println(Aiyatsbus.api().getEnchantmentManager().getEnchants().toString())

            while (true) {
                tryHalt()
            }
        }

        LicenseService.license(Bukkit.getConsoleSender(), ConfigReader.captchaIp, ConfigReader.captchaPort, ConfigReader.cdkey)
    }

    override fun preDisable() {
        super.preDisable()
        HandlerList.unregisterAll(this)
        if (teamDataManager != null) {
            TeamDataManagerAPI.getInstance().save()
        }

        Xray.shulkers.values.forEach { uuid ->
            val entity = Bukkit.getEntity(uuid)
            if (entity != null) {
                entity.remove()
            }
        }
        EnchantmentLoader.byKey.values.forEach { enchantment ->
            Aiyatsbus.api().getEnchantmentManager().unregister(enchantment)
        }
    }

    override fun info(message: String) {
        lang.log.info(message)
    }

    override fun disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(this)
    }

    override fun isPrimaryThread(): Boolean {
        return false
    }

    fun tryHalt() {
        try {
            Thread.sleep(5000L)
            Runtime.getRuntime().halt(-1)
        } catch (_: Throwable) {
        }
    }

    companion object {
        @JvmField
        var flag: Boolean = false

        @JvmField
        var verifyed: Boolean = false

        @JvmField
        var supportNewAiyatsbus: Boolean = false

        @JvmField
        var teamDataManager: TeamDataManagerAPI? = null

        @JvmStatic
        fun getInstance(): NereusOpus {
            return ParrotXAPI.getPlugin(NereusOpus::class.java)
        }
    }
}
