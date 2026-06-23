package nereusopuscore.utils

import hamsteryds.nereusopus.utils.TeamUtils
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

object TrieUtils {
    @JvmField
    var onlinePlayers: Trie? = null

    @JvmField
    var offlinePlayers: Trie? = null

    @JvmField
    var teamPlayerCommands: Trie? = null

    @JvmField
    var teamNames: Trie? = null

    @JvmStatic
    fun init() {
        teamPlayerCommands = Trie(
            listOf(
                "create",
                "get",
                "info",
                "join",
                "list",
                "quit"
            )
        )

        val online = Trie()
        for (player in Bukkit.getOnlinePlayers()) {
            online.addWord(player.name)
        }
        onlinePlayers = online

        val offline = Trie()
        for (player: OfflinePlayer in Bukkit.getOfflinePlayers()) {
            val name = player.name
            if (name != null) {
                offline.addWord(name)
            }
        }
        offlinePlayers = offline

        val teams = Trie()
        for (name in TeamUtils.getAllTeams()) {
            teams.addWord(name)
        }
        teamNames = teams
    }
}
