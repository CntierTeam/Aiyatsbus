package cc.polarastrum.aiyatsbus.core.data

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import taboolib.library.configuration.ConfigurationSection

data class VanillaInjector @JvmOverloads constructor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    var enable: Boolean = root.getBoolean("enable", false),
    val before: VanillaInjectionExecutor? = root.getConfigurationSection("before")?.let {
        VanillaInjectionExecutor(it, enchant)
    },
    val value: String = root.getString("value", "") ?: "",
    val after: VanillaInjectionExecutor? = root.getConfigurationSection("after")?.let {
        VanillaInjectionExecutor(it, enchant)
    }
)
