package c.mathias.microwave.presentation

sealed interface MainEvent {
    data object OpenDoor : MainEvent
    data object CloseDoor : MainEvent
    data object StartMicroWave : MainEvent
}