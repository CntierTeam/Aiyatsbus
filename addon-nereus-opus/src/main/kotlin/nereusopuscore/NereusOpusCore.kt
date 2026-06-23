package nereusopuscore

import hamsteryds.nereusopus.ConfigReader
import hamsteryds.nereusopus.NereusOpus
import hamsteryds.nereusopus.enchants.PublicTasks
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners
import hamsteryds.nereusopus.utils.FoliaUtils
import hamsteryds.nereusopus.utils.MechanismUtils
import nereusopuscore.commands.Commands
import nereusopuscore.utils.TrieUtils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

object NereusOpusCore {
    @JvmField
    var plugin: NereusOpus? = null

    @JvmStatic
    fun loadBasic() {
        if (ConfigReader.cdkey!!.contains("SummerIceBearStudio-MadeByHeart")) {
            FoliaUtils.runAtFixedRate({ _ ->
                var total = 0
                Bukkit.getOnlinePlayers().forEach { player ->
                    total++
                    if (total >= 6) {
                        player.kick(Component.text("§a附魔插件体验版仅允许五人(含NPC)及以下服务器使用\n§c如有更高玩家数量需求，请购买附魔插件正版！\n§e作者QQ: 3332366064 交流群: 953201353"))
                    }
                }
            }, 0L, 20L)
        }

        plugin = NereusOpus.getInstance()
        ListenerRegisterer.loadListeners()
        PublicTasks.initialize()
        plugin!!.registerCommand(Commands(plugin!!, "nereusopusteam"))
        if (NereusOpus.getInstance().isFolia) {
            Bukkit.getAsyncScheduler().runNow(NereusOpus.getInstance()) { TrieUtils.init() }
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(NereusOpus.getInstance(), Runnable { TrieUtils.init() })
        }

        Bukkit.getPluginManager().registerEvents(PlayerListener(), plugin!!)
        plugin!!.lang.log.info("Successfully Loaded NereusOpus! Author: 南外丶仓鼠(白熊)")
        plugin!!.lang.log.info("| ")
        plugin!!.lang.log.info("|- 感谢 次元互联 对本项目的支持 售前服务群：1064601581")
        plugin!!.lang.log.info("|- 感谢 YiMiner 对本项目的支持 强力宝石插件交流群：777905589")
        NereusOpus.verifyed = true
        FoliaUtils.runAtFixedRate({ _ ->
            CheckListeners.events.clear()
            BlockListener.extra.clear()
            MechanismUtils.cache.clear()
        }, 0L, 1200L)
    }

    @JvmStatic
    fun reload() {
    }

    @JvmStatic
    fun run() {
        FoliaUtils.runTask { loadBasic() }
    }
}
