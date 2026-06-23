package cc.polarastrum.aiyatsbus.impl.registration.v12100_paper

import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.inventory.ItemStack

class VanillaCraftEnchantment(
    private val enchant: VanillaAiyatsbusEnchantmentBase,
    holder: Enchantment
) : CraftEnchantment(enchant.enchantmentKey, holder), VanillaAiyatsbusEnchantment by enchant {

    init {
        enchant.enchantment = this
    }

    override fun getMaxLevel(): Int {
        return enchant.basicData.maxLevel
    }

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return enchant.conflictsWith(other)
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return enchant.canEnchantItem(item)
    }
}
