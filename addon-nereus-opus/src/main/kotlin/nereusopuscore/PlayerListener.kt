package nereusopuscore

import nereusopuscore.utils.TrieUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) {
        TrieUtils.onlinePlayers?.removeWord(event.player.name)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        TrieUtils.onlinePlayers?.addWord(event.player.name)
        TrieUtils.offlinePlayers?.addWord(event.player.name)
    }
}
