package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.EventFunctions
import taboolib.module.configuration.Configuration

open class BuiltinAiyatsbusEnchantmentBase(
    id: String,
    config: Configuration,
    eventExecutor: EventFunctions
) : AiyatsbusEnchantmentBase(id, null, config), BuiltinAiyatsbusEnchantment, EventFunctions by eventExecutor {

    override open val mechanism: Mechanism = Mechanism(null, this)
}
