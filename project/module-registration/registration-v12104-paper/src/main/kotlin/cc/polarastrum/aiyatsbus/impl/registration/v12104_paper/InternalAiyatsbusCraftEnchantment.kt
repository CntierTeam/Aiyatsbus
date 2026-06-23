package cc.polarastrum.aiyatsbus.impl.registration.v12104_paper

import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase
import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment

class InternalAiyatsbusCraftEnchantment(
    private val enchant: InternalAiyatsbusEnchantmentBase,
    holder: Holder<Enchantment>
) : AiyatsbusCraftEnchantment(enchant, holder), InternalAiyatsbusEnchantment by enchant
