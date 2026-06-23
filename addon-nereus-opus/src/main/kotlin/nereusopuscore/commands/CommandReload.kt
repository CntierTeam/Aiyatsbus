package nereusopuscore.commands

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import hamsteryds.nereusopus.enchants.EnchantmentLoader
import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import nereusopuscore.NereusOpusCore

class CommandReload @JvmOverloads constructor(
    plugin: PPlugin,
    perm: String,
    private val customRun: Runnable? = null
) : BaseCommand(plugin, "reload", 0) {
    init {
        perm(perm)
        describe("重载插件配置文件")
    }

    override fun call(args: Array<String>) {
        try {
            plugin.preDisable()
            val registerer = Aiyatsbus.api().getEnchantmentRegisterer()
            if (registerer is ModernEnchantmentRegisterer) {
                registerer.unfreezeRegistry()
                unregister()
                registerer.freezeRegistry()
            } else {
                unregister()
            }

            plugin.init()
            NereusOpusCore.loadBasic()
            if (customRun != null) {
                customRun.run()
            }

            sender.sendMessage(plugin.lang.data.info("重载插件成功."))
        } catch (throwable: Throwable) {
            sender.sendMessage(plugin.lang.data.info("重载插件失败, 请查看控制台中的错误信息."))
            lang.log.error("重载", "插件", throwable, plugin.packageName)
        }
    }

    companion object {
        private fun unregister() {
            EnchantmentLoader.byKey.values.forEach { enchantment ->
                Aiyatsbus.api().getEnchantmentManager().unregister(enchantment)
            }
            EnchantmentLoader.byKey.clear()
            EnchantmentLoader.loadEnchantments()
        }
    }
}
