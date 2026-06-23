package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.TeamUtils

class CommandList(plugin: PPlugin) : BaseCommand(plugin, "list", 0) {
    init {
        describe("列出所有队伍名称")
        perm(".team.list")
        mustPlayer(false)
    }

    override fun call(strings: Array<String>) {
        val teams = TeamUtils.getAllTeams().toString()
            .replace(Regex("^\\[*"), "")
            .replace(Regex("\\]+$"), "")
        if (teams.isEmpty()) {
            plugin.lang.sender.warn(sender, "team.list_empty")
        } else {
            plugin.lang.sender.info(sender, "team.list", teams)
        }
    }
}
