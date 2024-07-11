package c.mathias.microwave.controller

sealed interface MicrowaveDoorState {

    data object Open : MicrowaveDoorState

    data object Closed : MicrowaveDoorState

}
