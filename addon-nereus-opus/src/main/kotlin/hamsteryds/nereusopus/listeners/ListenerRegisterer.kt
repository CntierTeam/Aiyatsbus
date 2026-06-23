package hamsteryds.nereusopus.listeners

import org.bukkit.event.Listener

object ListenerRegisterer {
    @JvmField
    val listenersToRegister: MutableList<Listener> = ArrayList()

    @JvmStatic
    fun register(listener: Listener) {
        listenersToRegister.add(listener)
    }
}
