package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils
import org.bukkit.entity.Player

class CommandInfo(plugin: PPlugin) : BaseCommand(plugin, "info", 0) {
    init {
        describe("查询队伍信息")
        perm(".team.info")
        mustPlayer(false)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("队伍名称")
                .description("要查询的队伍的名称")
                .optional(false)
                .suggest { TrieUtils.teamNames!!.getAllValues().toTypedArray() }
                .converter { strings -> strings[0] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val teamName: String? = if (strings.isNotEmpty()) {
            convert(0, strings, String::class.java)
        } else if (sender is Player) {
            TeamUtils.getTeamName(user)
        } else {
            null
        }

        if (teamName == null) {
            if (sender is Player) {
                plugin.lang.sender.warn(user, "team.not_join")
            } else {
                plugin.lang.sender.warn(sender, "team.name_not_provided")
            }
        } else {
            val teamMates = TeamUtils.getMemberNames(teamName).toString()
                .replace(Regex("^\\[*"), "")
                .replace(Regex("\\]+$"), "")
            plugin.lang.sender.info(sender, "team.info_name", teamName)
            plugin.lang.sender.info(sender, "team.info_mates", teamMates)
        }
    }
}
