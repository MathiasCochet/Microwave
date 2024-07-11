package c.mathias.microwave.manager

import c.mathias.microwave.presentation.MicrowaveController

interface MicrowaveManager {
    suspend fun start(microwave: MicrowaveController)
    fun stop()
}