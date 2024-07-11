package c.mathias.microwave.presentation

data class MainUIState(
    val doorOpen: Boolean = false,
    val heaterOn: Boolean = false,
) {
    fun build(block: Builder.() -> Unit) = Builder(this).apply(block).build()

    class Builder(state: MainUIState) {
        var doorOpen: Boolean = state.doorOpen
        var heaterOn: Boolean = state.heaterOn

        fun build(): MainUIState {
            return MainUIState(
                doorOpen = doorOpen,
                heaterOn = heaterOn,
            )
        }
    }
}