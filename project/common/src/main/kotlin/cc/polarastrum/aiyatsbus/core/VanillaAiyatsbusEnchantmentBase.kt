package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.VanillaInjector
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism
import taboolib.module.configuration.Configuration
import java.io.File

class VanillaAiyatsbusEnchantmentBase(
    id: String,
    file: File?,
    config: Configuration
) : InternalAiyatsbusEnchantmentBase(id, file, config), VanillaAiyatsbusEnchantment {

    override val mechanism: Mechanism? = null

    override val injector: VanillaInjector? = config.getConfigurationSection("injector")?.let {
        VanillaInjector(it, this)
    }
}
