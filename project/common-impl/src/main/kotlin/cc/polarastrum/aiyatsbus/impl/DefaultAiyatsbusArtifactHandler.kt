package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.AiyatsbusArtifactHandler
import cc.polarastrum.aiyatsbus.core.sendLang
import org.bukkit.inventory.EquipmentSlot
import taboolib.library.configuration.ConfigurationSection
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

class DefaultAiyatsbusArtifactHandler : AiyatsbusArtifactHandler {

    override fun getSettings(): AiyatsbusArtifactHandler.Settings {
        return AiyatsbusArtifactSettings
    }

    @ConfigNode(bind = "enchants/artifact.yml")
    object AiyatsbusArtifactSettings : AiyatsbusArtifactHandler.Settings {

        @Config("enchants/artifact.yml", autoReload = true)
        override lateinit var conf: Configuration

        @ConfigNode("normal.range")
        override var range: List<Double> = emptyList()

        @ConfigNode("normal.height")
        override var height: List<Double> = emptyList()

        @delegate:ConfigNode("blocks")
        override val blocks: List<String> by conversion<List<String>, List<String>> { this }

        @delegate:ConfigNode("specialized")
        override val customParticleData: Map<EquipmentSlot, Pair<Double, Double>> by conversion<ConfigurationSection, Map<EquipmentSlot, Pair<Double, Double>>> {
            getKeys(false).associate { slot ->
                val section = getConfigurationSection(slot)!!
                EquipmentSlot.valueOf(slot.uppercase()) to (section.getDouble("range", 0.0) to section.getDouble("height", 0.0))
            }
        }

        @delegate:ConfigNode("specialized")
        override val customParticleType: Map<EquipmentSlot, String> by conversion<ConfigurationSection, Map<EquipmentSlot, String>> {
            getKeys(false).associate { slot ->
                EquipmentSlot.valueOf(slot.uppercase()) to getString("$slot.type", "SIMPLE")!!.uppercase()
            }
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusArtifactHandler>(DefaultAiyatsbusArtifactHandler())
            registerLifeCycleTask(LifeCycle.ENABLE) {
                AiyatsbusArtifactSettings.conf.onReload {
                    console().sendLang("configuration-reload", AiyatsbusArtifactSettings.conf.file!!.name, 0)
                }
            }
        }
    }
}
