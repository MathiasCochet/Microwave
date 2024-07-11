package c.mathias.microwave.presentation

import androidx.lifecycle.ViewModel
import c.mathias.microwave.manager.MicrowaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MicrowaveManager
) : ViewModel(), MicrowaveController {
    private val _doorStatusChanged = MutableSharedFlow<Boolean>()
    private val _startButtonPressed = MutableSharedFlow<Unit>()

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState

    fun initialize() {
        interactor.start(this)
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.CloseDoor -> {
                setState { doorOpen = false }
                _doorStatusChanged.tryEmit(uiState.value.doorOpen)
            }
            MainEvent.OpenDoor -> {
                setState { doorOpen = true}
                _doorStatusChanged.tryEmit(uiState.value.doorOpen)
            }
            MainEvent.StartMicroWave -> _startButtonPressed.tryEmit(Unit)
        }
    }

    override fun turnOnHeater() = setState { heaterOn = true }

    override fun turnOffHeater() = setState { heaterOn = false }

    override fun isDoorOpen(): Boolean = uiState.value.doorOpen

    override val doorStatusChanged: SharedFlow<Boolean> = _doorStatusChanged

    override val startButtonPressed: SharedFlow<Unit> = _startButtonPressed

    private fun setState(func: MainUIState.Builder.() -> Unit) {
        _uiState.value = _uiState.value.build(func)
    }
}