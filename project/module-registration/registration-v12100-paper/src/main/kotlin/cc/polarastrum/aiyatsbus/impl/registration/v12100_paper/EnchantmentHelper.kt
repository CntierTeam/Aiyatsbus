package cc.polarastrum.aiyatsbus.impl.registration.v12100_paper

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.enchantments.CraftEnchantment

object EnchantmentHelper {

    fun createCraftEnchantment(key: NamespacedKey, nms: Enchantment?): Any? {
        return CraftEnchantment(key, nms ?: return null)
    }

    fun createVanillaCraftEnchantment(enchant: AiyatsbusEnchantmentBase, nms: Enchantment): Any {
        require(enchant is VanillaAiyatsbusEnchantmentBase) {
            "Enchant ${enchant.id} must be an impl of VanillaAiyatsbusEnchantment!"
        }
        return VanillaCraftEnchantment(enchant, nms)
    }

    fun createAiyatsbusCraftEnchantment(enchant: AiyatsbusEnchantmentBase, nms: Enchantment): Any {
        return when (enchant) {
            is BuiltinAiyatsbusEnchantmentBase -> BuiltinAiyatsbusCraftEnchantment(enchant, nms)
            is InternalAiyatsbusEnchantmentBase -> InternalAiyatsbusCraftEnchantment(enchant, nms)
            else -> AiyatsbusCraftEnchantment(enchant, nms)
        }
    }
}
