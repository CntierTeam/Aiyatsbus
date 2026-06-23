package cc.polarastrum.aiyatsbus.core.data.trigger

enum class TriggerType(val isUnique: Boolean = false) {
    ARTIFACT(true),
    LISTENER,
    TICKER,
    SKILL,
    BUILTIN(true)
}
