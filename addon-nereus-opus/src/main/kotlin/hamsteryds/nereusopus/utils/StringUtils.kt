package hamsteryds.nereusopus.utils

import net.md_5.bungee.api.ChatColor

object StringUtils {
    @JvmStatic
    fun <E> replace(origin: String, vararg params: E): String {
        var result = origin
        var i = 0
        while (i < params.size) {
            result = result.replace(params[i].toString(), params[i + 1].toString())
            i += 2
        }
        return result
    }

    @JvmStatic
    fun replace(origin: String, params: Map<String, String>): String {
        var result = origin
        for (key in params.keys) {
            result = result.replace(key, params[key]!!)
        }
        return result
    }

    @JvmStatic
    fun keyToName(key: String): String {
        val name = StringBuilder()
        name.append(Character.toChars(key[0].code - ' '.code))

        var i = 1
        while (i < key.length) {
            if (key[i] == '_') {
                name.append(Character.toChars(key[i + 1].code - ' '.code))
                i += 2
            } else {
                name.append(key[i])
                i++
            }
        }

        return name.toString()
    }

    @JvmStatic
    fun removeFormat(legacy: String): String {
        return ChatColor.stripColor(legacy)
    }

    @JvmStatic
    fun upperFirstLetter(legacy: String): String {
        return legacy.substring(0, 1).lowercase() + legacy.substring(1)
    }
}
