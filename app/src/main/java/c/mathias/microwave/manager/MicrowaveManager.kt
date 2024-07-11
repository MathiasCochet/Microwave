package c.mathias.microwave.manager

import c.mathias.microwave.controller.MicrowaveController

interface MicrowaveManager {
    suspend fun start(microwave: MicrowaveController)
    fun stop()
}