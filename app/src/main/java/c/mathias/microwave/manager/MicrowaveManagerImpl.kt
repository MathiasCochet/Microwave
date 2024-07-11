package c.mathias.microwave.manager

import c.mathias.microwave.controller.MicrowaveController
import c.mathias.microwave.controller.MicrowaveDoorState
import c.mathias.microwave.tools.HeaterTimer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MicrowaveManagerImpl(
    private val heaterTimer: HeaterTimer,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MicrowaveManager {
    private var job: Job? = null

    override suspend fun start(microwave: MicrowaveController) {
        job = CoroutineScope(coroutineDispatcher).launch {
            launch {
                listenToDoorStatus(microwave.doorStatusChanged) {
                    if (it == MicrowaveDoorState.Open) {
                        microwave.turnOffHeater()
                        heaterTimer.stop()
                    }
                }
            }
            launch {
                listenToStartButton(microwave.startButtonPressed) {
                    startButtonPressed(microwave)
                }
            }
        }
    }

    private suspend fun startButtonPressed(microwave: MicrowaveController) {
        if (microwave.isDoorOpen()) return

        if (!heaterTimer.finished()) {
            heaterTimer.increaseTime { microwave.turnOffHeater() }
        } else {
            microwave.turnOnHeater()
            heaterTimer.startTimer { microwave.turnOffHeater() }
        }

    }

    override fun stop() {
        job?.cancel()
    }

    private suspend fun listenToDoorStatus(
        doorStatus: SharedFlow<MicrowaveDoorState>,
        openDoor: (MicrowaveDoorState) -> Unit
    ) {
        doorStatus.collect {
            openDoor(it)
        }
    }

    private suspend fun listenToStartButton(
        startButtonPressed: SharedFlow<Unit>,
        buttonPressed: suspend () -> Unit
    ) {
        startButtonPressed.collect {
            buttonPressed()
        }
    }

}