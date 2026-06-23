package cc.polarastrum.aiyatsbus.impl.registration.v12103_paper

import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment

class InternalAiyatsbusCraftEnchantment(
    private val enchant: InternalAiyatsbusEnchantmentBase,
    nmsEnchantment: Enchantment
) : AiyatsbusCraftEnchantment(enchant, nmsEnchantment), InternalAiyatsbusEnchantment by enchant
