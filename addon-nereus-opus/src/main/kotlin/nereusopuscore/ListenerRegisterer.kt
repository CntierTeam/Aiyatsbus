package nereusopuscore

import hamsteryds.nereusopus.NereusOpus
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners
import hamsteryds.nereusopus.listeners.executors.entries.EntityListener
import hamsteryds.nereusopus.utils.TeamUtils
import org.bukkit.Bukkit

object ListenerRegisterer {
    @JvmStatic
    fun loadListeners() {
        val plugin = NereusOpus.getInstance()
        val pluginManager = Bukkit.getPluginManager()

        hamsteryds.nereusopus.listeners.ListenerRegisterer.listenersToRegister.forEach { listener ->
            pluginManager.registerEvents(listener, plugin)
        }

        TeamUtils.initialize()
        pluginManager.registerEvents(EntityListener(), plugin)
        pluginManager.registerEvents(BlockListener(), plugin)
        pluginManager.registerEvents(hamsteryds.nereusopus.listeners.executors.entries.PlayerListener(), plugin)
        pluginManager.registerEvents(CheckListeners(), plugin)
        hamsteryds.nereusopus.listeners.ListenerRegisterer.listenersToRegister.clear()
        pluginManager.registerEvents(ListenerRegistererContinuation(), plugin)
    }
}
