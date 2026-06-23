package cc.polarastrum.aiyatsbus.impl.registration.v12100_paper

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment

class BuiltinAiyatsbusCraftEnchantment(
    private val enchant: BuiltinAiyatsbusEnchantmentBase,
    nmsEnchantment: Enchantment
) : AiyatsbusCraftEnchantment(enchant, nmsEnchantment), BuiltinAiyatsbusEnchantment by enchant
