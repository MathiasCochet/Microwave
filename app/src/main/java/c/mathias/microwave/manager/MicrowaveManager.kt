package c.mathias.microwave.manager

import c.mathias.microwave.presentation.MicrowaveController

interface MicrowaveManager {
    fun start(microwave: MicrowaveController)
}