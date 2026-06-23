package cc.polarastrum.aiyatsbus.impl.registration.v12104_paper

import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase
import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment

class BuiltinAiyatsbusCraftEnchantment(
    private val enchant: BuiltinAiyatsbusEnchantmentBase,
    holder: Holder<Enchantment>
) : AiyatsbusCraftEnchantment(enchant, holder), BuiltinAiyatsbusEnchantment by enchant
