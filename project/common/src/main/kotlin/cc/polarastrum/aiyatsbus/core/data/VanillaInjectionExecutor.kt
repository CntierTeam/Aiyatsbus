package cc.polarastrum.aiyatsbus.core.data

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment
import cc.polarastrum.aiyatsbus.core.script.ScriptType
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection

data class VanillaInjectionExecutor @JvmOverloads constructor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    val handle: String = root.getString("handle")
        ?: root.getString("value")
        ?: root.getString("script")
        ?: "",
    val scriptType: ScriptType = parseScriptType(root)
) {

    val internalId: String = "VanillaEnchantment_${enchant.basicData.id}_VanillaInjector_${root.name}"

    init {
        if (handle.isNotBlank()) {
            try {
                Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType).preheat(handle, internalId)
            } catch (ex: Throwable) {
                warning("Unable to preheat the vanilla injector (${root.name}) of enchantment ${enchant.id}")
                ex.printStackTrace()
            }
        }
    }

    fun execute(entity: LivingEntity, vars: Map<String, Any?>): Any? {
        return Aiyatsbus.api().getScriptHandler().getScriptHandler(scriptType).invoke(handle, internalId, entity, vars)
    }

    companion object {

        private fun parseScriptType(root: ConfigurationSection): ScriptType {
            val configured = root.getString("script-type")
                ?: root.getString("script_type")
                ?: root.getString("type")
                ?: ScriptType.FLUXON.name
            return ScriptType.valueOf(configured.uppercase())
        }
    }
}
