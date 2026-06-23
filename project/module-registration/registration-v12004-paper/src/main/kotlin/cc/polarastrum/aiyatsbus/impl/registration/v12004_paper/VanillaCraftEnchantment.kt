package cc.polarastrum.aiyatsbus.impl.registration.v12004_paper

import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment
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

    override fun getTranslationKey(): String {
        return enchant.basicData.id
    }

    override fun translationKey(): String {
        return enchant.basicData.id
    }
}
