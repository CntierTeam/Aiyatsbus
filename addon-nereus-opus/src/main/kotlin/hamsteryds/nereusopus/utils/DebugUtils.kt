package hamsteryds.nereusopus.utils

import hamsteryds.nereusopus.ConfigReader
import org.bukkit.entity.LivingEntity

object DebugUtils {
    @JvmField
    var debugEnabled: Boolean = false

    @JvmField
    var debuggers: List<String>? = null

    @JvmStatic
    fun initialize() {
        debugEnabled = ConfigReader.enableDebug
        debuggers = ConfigReader.debugPlayers
    }

    @JvmStatic
    fun debug(msg: String, from: LivingEntity) {
        if (debugEnabled && debuggers!!.contains(from.name)) {
            from.sendMessage(msg)
        }
    }
}
