package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.utils.TeamUtils
import nereusopuscore.utils.TrieUtils

class CommandCreate(plugin: PPlugin) : BaseCommand(plugin, "create", 1) {
    init {
        describe("创建队伍")
        perm(".team.create")
        mustPlayer(true)
        addParam(
            CommandParam.builder()
                .position(0)
                .name("队伍名称")
                .description("要创建的队伍的名称")
                .optional(false)
                .suggest { arrayOf("<队伍名称>") }
                .converter { strings -> strings[0] }
                .build()
        )
    }

    override fun call(strings: Array<String>) {
        val name: String = convert(0, strings, String::class.java)!!
        if (!sender.isOp && TeamUtils.getTeamName(user) != null) {
            plugin.lang.sender.error(user, "team.create_failed")
        } else if (TeamUtils.isTeamExist(name)) {
            plugin.lang.sender.warn(user, "team.name_exist")
        } else {
            TeamUtils.createTeam(name)
            TrieUtils.teamNames!!.addWord(name)
            if (!sender.isOp) {
                TeamUtils.addMember(name, user)
            }

            plugin.lang.sender.info(user, "team.create_succeed", name)
        }
    }
}
