package cc.polarastrum.aiyatsbus.impl.registration.legacy

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase

class LegacyBuiltinAiyatsbusCraftEnchantment(
    private val enchant: BuiltinAiyatsbusEnchantmentBase
) : LegacyAiyatsbusCraftEnchantment(enchant), BuiltinAiyatsbusEnchantment by enchant
