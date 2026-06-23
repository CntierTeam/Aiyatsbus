package cc.polarastrum.aiyatsbus.core

import org.bukkit.inventory.EquipmentSlot
import taboolib.module.configuration.Configuration

interface AiyatsbusArtifactHandler {

    fun getSettings(): Settings

    interface Settings {
        var conf: Configuration
        var range: List<Double>
        var height: List<Double>
        val blocks: List<String>
        val customParticleData: Map<EquipmentSlot, Pair<Double, Double>>
        val customParticleType: Map<EquipmentSlot, String>
    }
}
