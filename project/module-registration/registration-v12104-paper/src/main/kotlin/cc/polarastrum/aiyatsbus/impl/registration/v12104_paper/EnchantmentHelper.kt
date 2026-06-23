package cc.polarastrum.aiyatsbus.impl.registration.v12104_paper

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantmentBase
import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.enchantments.CraftEnchantment

object EnchantmentHelper {

    fun createCraftEnchantment(nms: Holder<Enchantment>): Any {
        return CraftEnchantment(nms)
    }

    fun createVanillaCraftEnchantment(enchant: AiyatsbusEnchantmentBase, nms: Holder<Enchantment>): Any {
        require(enchant is VanillaAiyatsbusEnchantmentBase) {
            "Enchant ${enchant.id} must be an impl of VanillaAiyatsbusEnchantment!"
        }
        return VanillaCraftEnchantment(enchant, nms)
    }

    fun createAiyatsbusCraftEnchantment(enchant: AiyatsbusEnchantmentBase, nms: Holder<Enchantment>): Any {
        return when (enchant) {
            is BuiltinAiyatsbusEnchantmentBase -> BuiltinAiyatsbusCraftEnchantment(enchant, nms)
            is InternalAiyatsbusEnchantmentBase -> InternalAiyatsbusCraftEnchantment(enchant, nms)
            else -> AiyatsbusCraftEnchantment(enchant, nms)
        }
    }
}
