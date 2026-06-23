package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.TeamUtils

class CommandQuit(plugin: PPlugin) : BaseCommand(plugin, "quit", 0) {
    init {
        describe("退出当前队伍")
        perm(".team.quit")
        mustPlayer(true)
    }

    override fun call(strings: Array<String>) {
        val teamName = TeamUtils.getTeamName(user)
        if (teamName == null) {
            plugin.lang.sender.warn(user, "team.not_join")
        } else {
            TeamUtils.removeMember(user)
            plugin.lang.sender.info(user, "team.quit_succeed", teamName)
        }
    }
}
