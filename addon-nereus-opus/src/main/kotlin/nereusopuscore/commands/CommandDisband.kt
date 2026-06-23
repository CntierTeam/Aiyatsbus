package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.MessageUtils
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CommandDisband(plugin: PPlugin) : BaseCommand(plugin, "disband", 1) {
    init {
        describe("解散队伍")
        perm(".team.disband")
        mustPlayer(false)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("队伍名称")
                .description("要解散的队伍的名称")
                .optional(false)
                .suggest { TrieUtils.teamNames!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[0] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val teamName: String = convert(0, strings, String::class.java)!!
        if (!TeamUtils.isTeamExist(teamName)) {
            plugin.lang.sender.warn(sender, "team.not_exist")
        } else {
            val members = TeamUtils.getMembers(teamName)
            TeamUtils.removeTeam(teamName)
            TrieUtils.teamNames!!.removeWord(teamName)
            plugin.lang.sender.info(sender, "team.disband_succeed", teamName)

            for (uuid in members) {
                if (sender !is Player || uuid != user.uniqueId) {
                    MessageUtils.sendMessage(Bukkit.getOfflinePlayer(uuid), plugin.lang.data.getInfo("team.be_disbanded", teamName))
                }
            }
        }
    }
}
