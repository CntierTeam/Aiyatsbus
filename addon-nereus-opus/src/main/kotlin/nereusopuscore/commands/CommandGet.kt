package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.Bukkit

class CommandGet(plugin: PPlugin) : BaseCommand(plugin, "get", 1) {
    init {
        describe("查询玩家所在队伍名称")
        perm(".team.get")
        mustPlayer(false)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("玩家名")
                .description("要查询的玩家的名称")
                .optional(false)
                .suggest { TrieUtils.offlinePlayers!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[0] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val name: String = convert(0, strings, String::class.java)!!
        if (!Bukkit.getOfflinePlayer(name).hasPlayedBefore() && !Bukkit.getOfflinePlayer(name).isOnline) {
            plugin.lang.sender.info(sender, "player_not_exist", name)
        } else {
            val player = Bukkit.getOfflinePlayer(name)
            val teamName = TeamUtils.getTeamName(player.uniqueId)
            if (teamName == null) {
                plugin.lang.sender.warn(sender, "team.player_not_join", name)
            } else {
                plugin.lang.sender.info(sender, "team.player_get_team", name, teamName)
            }
        }
    }
}
