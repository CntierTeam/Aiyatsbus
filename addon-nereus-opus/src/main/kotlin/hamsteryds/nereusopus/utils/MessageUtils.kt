package hamsteryds.nereusopus.utils

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

object MessageUtils {
    private val messages: MutableMap<UUID, MutableList<String>> = HashMap()

    @JvmStatic
    fun sendMessage(player: Player, message: String) {
        player.sendMessage(message)
    }

    @JvmStatic
    fun sendMessage(player: OfflinePlayer, message: String) {
        if (player.isOnline) {
            (player as Player).sendMessage(message)
        } else {
            messages.putIfAbsent(player.uniqueId, ArrayList())
            messages[player.uniqueId]!!.add(message)
        }
    }

    @JvmStatic
    fun sendMessage(uuid: UUID, message: String) {
        sendMessage(Bukkit.getOfflinePlayer(uuid), message)
    }

    @JvmStatic
    fun sync(player: Player) {
        if (messages.containsKey(player.uniqueId)) {
            for (message in messages[player.uniqueId]!!) {
                player.sendMessage(message)
            }

            messages.remove(player.uniqueId)
        }
    }
}
