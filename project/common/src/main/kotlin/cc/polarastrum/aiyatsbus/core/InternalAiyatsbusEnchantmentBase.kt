package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import taboolib.module.configuration.Configuration
import java.io.File

open class InternalAiyatsbusEnchantmentBase(
    id: String,
    file: File?,
    config: Configuration
) : AiyatsbusEnchantmentBase(id, file, config), InternalAiyatsbusEnchantment {

    override open val mechanism: Mechanism? = Mechanism(config.getConfigurationSection("mechanisms"), this)
}
