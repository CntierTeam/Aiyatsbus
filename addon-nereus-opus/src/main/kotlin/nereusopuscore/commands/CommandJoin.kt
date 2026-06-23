package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.MessageUtils
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.Bukkit

class CommandJoin(plugin: PPlugin) : BaseCommand(plugin, "join", 1) {
    init {
        describe("加入队伍")
        perm(".team.join")
        mustPlayer(true)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("队伍名称")
                .description("要加入的队伍的名称")
                .optional(false)
                .suggest { TrieUtils.teamNames!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[0] }
                .build()
        )
        addParam(
            CommandParam.builder()
                .position(1)
                .name("玩家名")
                .description("要操作加入的玩家的名称")
                .optional(true)
                .suggest { TrieUtils.offlinePlayers!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[1] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val name: String = convert(0, strings, String::class.java)!!
        if (strings.size == 2) {
            if (sender.isOp) {
                val targetName: String = convert(1, strings, String::class.java)!!
                if (!Bukkit.getOfflinePlayer(targetName).hasPlayedBefore() && !Bukkit.getOfflinePlayer(targetName).isOnline) {
                    plugin.lang.sender.warn(user, "player_not_exist", targetName)
                } else {
                    val player = Bukkit.getOfflinePlayer(targetName)
                    if (!TeamUtils.isTeamExist(name)) {
                        plugin.lang.sender.warn(user, "team.not_exist")
                    } else {
                        TeamUtils.addMember(name, player.uniqueId)
                        plugin.lang.sender.info(user, "team.join_by_op_succeed", targetName, name)
                        MessageUtils.sendMessage(player, plugin.lang.data.getInfo("team.joined_by_op", name))
                    }
                }
            }
        } else if (!TeamUtils.isTeamExist(name)) {
            plugin.lang.sender.warn(user, "team.not_exist")
        } else {
            TeamUtils.addMember(name, user)
            plugin.lang.sender.info(user, "team.join_succeed", name)
        }
    }
}
