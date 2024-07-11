package c.mathias.microwave.manager

import c.mathias.microwave.presentation.MicrowaveController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class MicrowaveManagerImpl(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MicrowaveManager {
    private var job: Job? = null

    override suspend fun start(microwave: MicrowaveController) {
        job = CoroutineScope(coroutineDispatcher).launch {
            launch {
                listenToDoorStatus(microwave.doorStatusChanged) { doorIsClosed ->
                    if (!doorIsClosed) microwave.turnOffHeater()
                }
            }
            launch {
                listenToStartButton(microwave.startButtonPressed) {
                    if (!microwave.isDoorOpen()) microwave.turnOnHeater()
                }
            }
        }
    }

    override fun stop() {
        job?.cancel()
    }

    private suspend fun listenToDoorStatus(
        doorStatus: SharedFlow<Boolean>,
        openDoor: (Boolean) -> Unit
    ) {
        doorStatus.collect {
            openDoor(it)
        }
    }

    private suspend fun listenToStartButton(
        startButtonPressed: SharedFlow<Unit>,
        buttonPressed: () -> Unit
    ) {
        startButtonPressed.collect {
            buttonPressed()
        }
    }

}