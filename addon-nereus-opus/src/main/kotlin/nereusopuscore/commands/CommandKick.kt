package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.MessageUtils
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.Bukkit

class CommandKick(plugin: PPlugin) : BaseCommand(plugin, "kick", 1) {
    init {
        describe("将玩家踢出队伍")
        perm(".team.kick")
        mustPlayer(false)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("玩家名")
                .description("要踢出的玩家的名称")
                .optional(false)
                .suggest { TrieUtils.offlinePlayers!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[0] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val targetName: String = convert(1, strings, String::class.java)!!
        if (!Bukkit.getOfflinePlayer(targetName).hasPlayedBefore() && !Bukkit.getOfflinePlayer(targetName).isOnline) {
            plugin.lang.sender.warn(user, "player_not_exist", targetName)
        } else {
            val player = Bukkit.getOfflinePlayer(targetName)
            if (TeamUtils.getTeamName(player.uniqueId) == null) {
                plugin.lang.sender.warn(user, "team.player_not_in_team", targetName)
            } else {
                TeamUtils.removeMember(targetName, player.uniqueId)
                plugin.lang.sender.info(user, "team.kick_succeed", targetName)
                MessageUtils.sendMessage(player, plugin.lang.data.getInfo("team.be_kicked", TeamUtils.getTeamName(player.uniqueId)))
            }
        }
    }
}
