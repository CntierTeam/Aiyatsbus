package cc.polarastrum.aiyatsbus.core.data.trigger.artifact

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.data.trigger.TriggerType
import cc.polarastrum.aiyatsbus.core.data.trigger.builtin.Builtin
import cc.polarastrum.aiyatsbus.core.util.spawnCircleParticles
import cc.polarastrum.aiyatsbus.core.util.spawnRNAParticles
import cc.polarastrum.aiyatsbus.core.util.spawnSimpleParticle
import org.bukkit.Location
import org.bukkit.entity.Ageable
import org.bukkit.entity.Ghast
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Slime
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.particles.XParticle

class Artifact @JvmOverloads constructor(
    root: ConfigurationSection,
    enchant: AiyatsbusEnchantment,
    val particle: XParticle? = XParticle.of(root.getString("particle.type")).orElse(null),
    val amount: Int = root.getInt("particle.amount", 1),
    val specialized: Boolean = root.getBoolean("particle.specialized", false),
    val options: Any? = null
) : Builtin(enchant, root, TriggerType.ARTIFACT) {

    override fun tickTask(level: Int, slot: EquipmentSlot, player: Player, stamp: Int) {
        if (specialized) {
            spawnSpecializedParticle(player.location, slot)
        } else {
            if (player.isGliding && slot == EquipmentSlot.CHEST) {
                spawnSimpleParticle(player.location)
            }
            if (slot == EquipmentSlot.FEET) {
                spawnCircleParticle(player.location.clone().add(0.0, 0.2, 0.0), 2)
            }
        }
    }

    override fun blockBreak(level: Int, event: BlockBreakEvent) {
        val blocks = Aiyatsbus.api().getArtifactHandler().getSettings().blocks
        if ("*" in blocks || event.block.type.name.lowercase() in blocks || event.block.type.name in blocks) {
            spawnSimpleParticle(event.block.location.clone().add(0.5, 0.5, 0.5))
        }
    }

    override fun attackEntity(level: Int, event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player
        if (player != null && player.attackCooldown < 0.9f) return
        val entity = event.entity as? LivingEntity ?: return
        val size = when (entity) {
            is Slime -> entity.size.coerceAtMost(3)
            is Ageable -> if (entity.isAdult) 2 else 1
            is Ghast -> 3
            else -> 2
        }
        spawnRNAParticle(entity.location.clone().add(0.0, 1.0, 0.0), size)
    }

    fun spawnRNAParticle(location: Location, size: Int) {
        val settings = Aiyatsbus.api().getArtifactHandler().getSettings()
        particle?.let {
            spawnRNAParticles(
                it,
                location,
                amount,
                options,
                settings.height[size - 1],
                settings.range[size - 1]
            )
        }
    }

    fun spawnSimpleParticle(location: Location) {
        particle?.let { spawnSimpleParticle(it, location, amount, options) }
    }

    fun spawnCircleParticle(location: Location, size: Int) {
        val settings = Aiyatsbus.api().getArtifactHandler().getSettings()
        particle?.let { spawnCircleParticles(it, location, amount, options, settings.range[size - 1]) }
    }

    fun spawnSpecializedParticle(location: Location, slot: EquipmentSlot) {
        val type = Aiyatsbus.api().getArtifactHandler().getSettings().customParticleType[slot] ?: return
        val (range, height) = Aiyatsbus.api().getArtifactHandler().getSettings().customParticleData[slot] ?: return
        val baseHeight = when (slot) {
            EquipmentSlot.HAND, EquipmentSlot.OFF_HAND -> 1.2
            EquipmentSlot.FEET -> 0.1
            EquipmentSlot.LEGS -> 0.6
            EquipmentSlot.CHEST -> 1.15
            EquipmentSlot.HEAD -> 1.8
            else -> 1.825
        }
        val loc = location.clone().add(0.0, baseHeight, 0.0)
        when (type.uppercase()) {
            "SIMPLE" -> spawnSimpleParticle(loc)
            "RNA" -> particle?.let { spawnRNAParticles(it, loc, amount, options, height, range, 10, 2) }
            "CIRCLE" -> particle?.let { spawnCircleParticles(it, loc.add(0.0, height, 0.0), amount, options, range) }
        }
    }
}
