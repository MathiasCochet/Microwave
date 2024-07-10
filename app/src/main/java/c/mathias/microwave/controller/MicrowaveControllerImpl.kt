package c.mathias.microwave.controller

import kotlinx.coroutines.flow.SharedFlow

class MicrowaveControllerImpl : MicrowaveController {
    override fun turnOnHeater() {
        TODO("Not yet implemented")
    }

    override fun turnOffHeater() {
        TODO("Not yet implemented")
    }

    override fun isDoorOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override val doorStatusChanged: SharedFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val startButtonPressed: SharedFlow<Unit>
        get() = TODO("Not yet implemented")
}