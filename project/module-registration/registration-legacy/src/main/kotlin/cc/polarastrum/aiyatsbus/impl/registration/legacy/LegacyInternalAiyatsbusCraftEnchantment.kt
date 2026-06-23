package cc.polarastrum.aiyatsbus.impl.registration.legacy

import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase

class LegacyInternalAiyatsbusCraftEnchantment(
    private val enchant: InternalAiyatsbusEnchantmentBase
) : LegacyAiyatsbusCraftEnchantment(enchant), InternalAiyatsbusEnchantment by enchant
