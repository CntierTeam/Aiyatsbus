package nereusopuscore.commands

import hamsteryds.nereusopus.library.parrotx.PPlugin
import hamsteryds.nereusopus.library.parrotx.command.BaseCommand
import hamsteryds.nereusopus.library.parrotx.command.CommandHandler
import hamsteryds.nereusopus.library.parrotx.command.subcommands.DebugCommand
import hamsteryds.nereusopus.library.parrotx.command.subcommands.HelpCommand
import hamsteryds.nereusopus.library.parrotx.command.subcommands.VersionCommand

class Commands(plugin: PPlugin, mainCmd: String) : CommandHandler(plugin, mainCmd) {
    init {
        addCommand("debug", t(DebugCommand(plugin, ".debug")))
        addCommand("help", t(HelpCommand(plugin)))
        addCommand("reload", t(CommandReload(plugin, ".reload")))
        addCommand("version", t(VersionCommand(plugin)))
        addCommand("create", t(CommandCreate(plugin)))
        addCommand("disband", t(CommandDisband(plugin)))
        addCommand("disbandall", t(CommandDisbandAll(plugin)))
        addCommand("get", t(CommandGet(plugin)))
        addCommand("info", t(CommandInfo(plugin)))
        addCommand("join", t(CommandJoin(plugin)))
        addCommand("kick", t(CommandKick(plugin)))
        addCommand("kickall", t(CommandKickAll(plugin)))
        addCommand("list", t(CommandList(plugin)))
        addCommand("quit", t(CommandQuit(plugin)))
    }

    private fun t(command: BaseCommand): BaseCommand {
        command.setHandler(this)
        return command
    }
}
