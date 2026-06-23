package hamsteryds.nereusopus

import hamsteryds.nereusopus.library.parrotx.config.PConfig
import hamsteryds.nereusopus.library.parrotx.data.autoload.annotations.PAutoload
import hamsteryds.nereusopus.library.parrotx.data.autoload.annotations.PAutoloadGroup
import hamsteryds.nereusopus.library.parrotx.data.autoload.annotations.PAutoloadGroups

@PAutoloadGroups(
    PAutoloadGroup(""),
    PAutoloadGroup(name = "captcha", value = "captcha"),
    PAutoloadGroup(name = "supports", value = "supports"),
    PAutoloadGroup(name = "world_group", value = "world_group"),
    PAutoloadGroup(name = "fixers", value = "fixers"),
    PAutoloadGroup(name = "team", value = "team"),
    PAutoloadGroup(name = "debug", value = "debug")
)
class ConfigReader : PConfig(NereusOpus.getInstance(), "config", "主配置文件") {
    override fun save() {
    }

    companion object {
        private var instance: ConfigReader? = null

        @JvmField
        @PAutoload("cdkey")
        var cdkey: String? = null

        @JvmField
        @PAutoload(group = "captcha", value = "ip")
        var captchaIp: String? = null

        @JvmField
        @PAutoload(group = "captcha", value = "port")
        var captchaPort: Int = 0

        @JvmField
        @PAutoload(group = "supports", value = "itemsadder")
        var supportItemsAdder: Boolean = false

        @JvmField
        @PAutoload(group = "supports", value = "citizens")
        var supportCitizens: Boolean = false

        @JvmField
        @PAutoload(group = "world_group", value = "no_pvp")
        var noPvpGroup: List<String>? = null

        @JvmField
        @PAutoload(group = "fixers", value = "bloodfixer")
        var bloodFixer: Boolean = false

        @JvmField
        @PAutoload(group = "fixers", value = "speedfixer")
        var speedFixer: Boolean = false

        @JvmField
        @PAutoload(group = "team", value = "damage.enable")
        var teamEnableDamage: Boolean = false

        @JvmField
        @PAutoload(group = "team", value = "savepath")
        var teamSavePath: String? = null

        @JvmField
        @PAutoload(group = "team", value = "savethreshold")
        var teamSaveThreshold: Int = 0

        @JvmField
        @PAutoload(group = "team", value = "placeholder")
        var teamPlaceholder: String? = null

        @JvmField
        @PAutoload(group = "debug", value = "enable")
        var enableDebug: Boolean = false

        @JvmField
        @PAutoload(group = "debug", value = "players")
        var debugPlayers: List<String>? = null

        @JvmStatic
        fun getInstance(): ConfigReader {
            if (instance == null) {
                instance = ConfigReader()
            }
            return instance!!
        }
    }
}
