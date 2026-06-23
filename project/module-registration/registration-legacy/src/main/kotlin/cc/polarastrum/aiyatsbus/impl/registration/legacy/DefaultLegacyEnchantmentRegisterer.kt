package cc.polarastrum.aiyatsbus.impl.registration.legacy

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.BuiltinAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.InternalAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.VanillaAiyatsbusEnchantmentBase
import cc.polarastrum.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import org.bukkit.enchantments.Enchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxyClass

object DefaultLegacyEnchantmentRegisterer : AiyatsbusEnchantmentRegisterer {

    val clazzLegacyVanillaCraftEnchantment =
        nmsProxyClass<Enchantment>(DefaultLegacyEnchantmentRegisterer::class.java.packageName + ".LegacyVanillaCraftEnchantment")

    @Awake(LifeCycle.CONST)
    fun init() {
        if (MinecraftVersion.versionId <= 12002) {
            Enchantment::class.java.setProperty("acceptingNew", value = true, isStatic = true)
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun exit() {
        if (MinecraftVersion.versionId <= 12002) {
            Enchantment::class.java.setProperty("acceptingNew", value = false, isStatic = true)
        }
    }

    override fun register(enchant: AiyatsbusEnchantmentBase): Enchantment {
        val enchantment = if (enchant.alternativeData.isVanilla) {
            require(enchant is VanillaAiyatsbusEnchantmentBase) {
                "Enchant ${enchant.id} must be an impl of VanillaAiyatsbusEnchantment!"
            }
            val bukkitEnchantment = Enchantment.getByKey(enchant.enchantmentKey)!!
            clazzLegacyVanillaCraftEnchantment
                .getConstructor(VanillaAiyatsbusEnchantmentBase::class.java, Enchantment::class.java)
                .newInstance(enchant, bukkitEnchantment).also {
                    Enchantment::class.java.getProperty<HashMap<*, *>>("byKey", true)!!.remove(bukkitEnchantment.key)
                    Enchantment::class.java.getProperty<HashMap<*, *>>("byName", true)!!.remove(bukkitEnchantment.name)
                }
        } else {
            when (enchant) {
                is BuiltinAiyatsbusEnchantmentBase -> LegacyBuiltinAiyatsbusCraftEnchantment(enchant)
                is InternalAiyatsbusEnchantmentBase -> LegacyInternalAiyatsbusCraftEnchantment(enchant)
                else -> LegacyAiyatsbusCraftEnchantment(enchant)
            }
        }
        Enchantment.registerEnchantment(enchantment)
        return enchantment
    }

    override fun unregister(enchant: AiyatsbusEnchantment) {
        if (!enchant.alternativeData.isVanilla) {
            Enchantment::class.java.getProperty<HashMap<*, *>>("byKey", true)!!.remove(enchant.enchantmentKey)
            Enchantment::class.java.getProperty<HashMap<*, *>>("byName", true)!!.remove(enchant.id.uppercase())
        } else if (enchant is VanillaAiyatsbusEnchantment) {
            enchant.injector?.enable = false
        }
    }
}
