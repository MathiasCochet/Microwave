package c.mathias.microwave.presentation

import kotlinx.coroutines.flow.SharedFlow

/** Interface to the microwave oven. */

interface MicrowaveController {
    /** Turns on the microwave heater element. */
    fun turnOnHeater()

    /** Turns off the microwave heater element. */
    fun turnOffHeater()

    /** Indicates if the door to the microwave is open or
    closed. */
    fun isDoorOpen(): Boolean

    /** Signals that the door is opened or closed */
    val doorStatusChanged: SharedFlow<Boolean>

    /** Signals that the start button is pressed */
    val startButtonPressed: SharedFlow<Unit>
}