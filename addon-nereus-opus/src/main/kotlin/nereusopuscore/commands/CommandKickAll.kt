package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.MessageUtils
import hamsteryds.nereusopus.utils.TeamUtils
import org.bukkit.Bukkit

class CommandKickAll(plugin: PPlugin) : BaseCommand(plugin, "kickall", 0) {
    init {
        describe("将所有玩家踢出队伍")
        perm(".team.kickall")
        mustPlayer(false)
    }

    override fun call(strings: Array<String>) {
        for (player in Bukkit.getOfflinePlayers()) {
            if (TeamUtils.getTeamName(player.uniqueId) != null) {
                MessageUtils.sendMessage(player, plugin.lang.data.getInfo("team.be_kicked", TeamUtils.getTeamName(player.uniqueId)))
                TeamUtils.removeMember(player.uniqueId)
            }
        }

        plugin.lang.sender.info(sender, "team.kick_all_succeed")
    }
}
