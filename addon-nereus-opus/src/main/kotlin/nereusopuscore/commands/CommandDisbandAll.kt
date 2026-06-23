package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.MessageUtils
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CommandDisbandAll(plugin: PPlugin) : BaseCommand(plugin, "disbandall", 0) {
    init {
        describe("解散所有队伍")
        perm(".team.disbandall")
        mustPlayer(false)
    }

    override fun call(strings: Array<String>) {
        for (teamName in TeamUtils.getAllTeams()) {
            val members = TeamUtils.getMembers(teamName)
            TeamUtils.removeTeam(teamName)
            TrieUtils.teamNames!!.removeWord(teamName)

            for (uuid in members) {
                if (sender !is Player || uuid != user.uniqueId) {
                    MessageUtils.sendMessage(Bukkit.getOfflinePlayer(uuid), plugin.lang.data.getInfo("team.be_disbanded", teamName))
                }
            }
        }

        plugin.lang.sender.info(sender, "team.disband_all_succeed")
    }
}
