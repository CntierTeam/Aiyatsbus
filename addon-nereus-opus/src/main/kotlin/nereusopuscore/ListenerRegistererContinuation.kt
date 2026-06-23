package nereusopuscore

import hamsteryds.nereusopus.utils.FoliaUtils
import hamsteryds.nereusopus.utils.MessageUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ListenerRegistererContinuation : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        FoliaUtils.runDelayed(
            { MessageUtils.sync(event.player) },
            10L
        )
    }
}
