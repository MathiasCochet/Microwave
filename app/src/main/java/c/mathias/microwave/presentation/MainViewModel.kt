package c.mathias.microwave.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import c.mathias.microwave.controller.MicrowaveController
import c.mathias.microwave.controller.MicrowaveDoorState
import c.mathias.microwave.manager.MicrowaveManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val microwaveManager: MicrowaveManager
) : ViewModel(), MicrowaveController {

    private val _doorStatusChanged = MutableSharedFlow<MicrowaveDoorState>()
    private val _startButtonPressed = MutableSharedFlow<Unit>()

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState

    fun initialize() {
        viewModelScope.launch {
            microwaveManager.start(this@MainViewModel)
        }
    }

    override fun onCleared() {
        super.onCleared()
        microwaveManager.stop()
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.CloseDoor -> {
                setState { doorOpen = false }
                viewModelScope.launch {
                    _doorStatusChanged.emit(MicrowaveDoorState.Closed)
                }
            }

            MainEvent.OpenDoor -> {
                setState { doorOpen = true }
                viewModelScope.launch {
                    _doorStatusChanged.emit(MicrowaveDoorState.Open)
                }
            }

            MainEvent.StartMicroWave -> viewModelScope.launch {
                _startButtonPressed.emit(Unit)
            }
        }
    }

    override fun turnOnHeater() = setState { heaterOn = true }

    override fun turnOffHeater() = setState { heaterOn = false }

    override fun isDoorOpen(): Boolean = uiState.value.doorOpen

    override val doorStatusChanged: SharedFlow<MicrowaveDoorState> get() = _doorStatusChanged

    override val startButtonPressed: SharedFlow<Unit> get() = _startButtonPressed

    private fun setState(func: MainUIState.Builder.() -> Unit) {
        _uiState.value = _uiState.value.build(func)
    }
}