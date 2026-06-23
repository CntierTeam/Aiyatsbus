package cc.polarastrum.aiyatsbus.module.ingame.command.subcommand

import cc.polarastrum.aiyatsbus.core.capability
import cc.polarastrum.aiyatsbus.core.sendLang
import cc.polarastrum.aiyatsbus.core.util.set
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.platform.util.modifyMeta

val capabilitySubCommand = subCommand {
    dynamic("operation") {
        suggestion<CommandSender> { _, _ -> listOf("set", "add", "take") }
        dynamic("amount") {
            suggestion<CommandSender> { _, _ -> listOf("1", "5", "10", "32") }
            execute<CommandSender> { sender, args, _ ->
                handleCapability(sender, null, args["operation"], args["amount"].toInt())
            }
            dynamic("player", true) {
                suggestPlayers()
                execute<CommandSender> { sender, args, _ ->
                    handleCapability(sender, args["player"], args["operation"], args["amount"].toInt())
                }
            }
        }
    }
}

private fun handleCapability(sender: CommandSender, who: String?, operation: String, amount: Int) {
    (who?.let { Bukkit.getPlayer(it) } ?: (sender as? Player))?.let { receiver ->
        val item = receiver.itemInHand
        item.modifyMeta<ItemMeta> {
            val update = when (operation) {
                "add" -> item.capability + amount
                "take" -> item.capability - amount
                else -> amount
            }
            this["aiyatsbus_item_capability", PersistentDataType.INTEGER] = update
            sender.sendLang("command-subCommands-capability-sender", receiver.name to "name", update to "amount")
            receiver.sendLang("command-subCommands-capability-receiver", update to "amount")
        }
    } ?: sender.sendLang("command-subCommands-capability-fail")
}
