package c.mathias.microwave.presentation

data class MainUIState(
    val lightOn: Boolean = false,
    val doorOpen: Boolean = false,
    val heaterOn: Boolean = false,
) {
    fun build(block: Builder.() -> Unit) = Builder(this).apply(block).build()

    class Builder(state: MainUIState) {
        var lightOn: Boolean = state.lightOn
        var doorOpen: Boolean = state.doorOpen
        var heaterOn: Boolean = state.heaterOn

        fun build(): MainUIState {
            return MainUIState(
                lightOn = lightOn,
                doorOpen = doorOpen,
                heaterOn = heaterOn,
            )
        }
    }
}