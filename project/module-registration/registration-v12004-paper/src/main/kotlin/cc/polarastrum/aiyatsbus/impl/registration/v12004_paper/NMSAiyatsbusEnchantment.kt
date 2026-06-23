/*
 * This file is part of EcoEnchants, licensed under the GPL-3.0 License.
 *
 *  Copyright (C) 2024 Auxilor
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cc.polarastrum.aiyatsbus.impl.registration.v12004_paper

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.world.inventory.ContainerAnvil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentSlotType
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.util.*

class NMSAiyatsbusEnchantment(val id: String) : net.minecraft.world.item.enchantment.Enchantment(
    Rarity.d,
    EnchantmentSlotType.n,
    emptyArray()
) {

    private val enchantmentKey: NamespacedKey = NamespacedKey.minecraft(id)

    private val enchant: AiyatsbusEnchantment?
        get() = Aiyatsbus.api().getEnchantmentManager().getEnchant(enchantmentKey)

    override fun a(stack: ItemStack): Boolean {
        val caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass
        if (caller.name == ContainerAnvil::class.java.name) {
            return false
        }

        val item = CraftItemStack.asCraftMirror(stack)
        return enchant?.canEnchantItem(item) ?: false
    }

    override fun e(): Int = 1

    override fun a(): Int = enchant?.basicData?.maxLevel ?: 1

    override fun c(): Boolean = false

    override fun i(): Boolean = false

    override fun h(): Boolean = false

    override fun b(): Boolean = true

    override fun d(level: Int): IChatBaseComponent {
        return if (enchant != null) {
            IChatBaseComponent::class.java.invokeMethod<IChatBaseComponent>(
                "a",
                enchant!!.displayName(level),
                remap = false
            )!!
        } else {
            super.d(level)
        }
    }

    override fun toString(): String {
        return "NMSAiyatsbusEnchantment(id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        return other is NMSAiyatsbusEnchantment && other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}
