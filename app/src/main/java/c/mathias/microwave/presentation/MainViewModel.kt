package c.mathias.microwave.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val interactor: MicrowaveManager
) : ViewModel(), MicrowaveController {
    private val _doorStatusChanged = MutableSharedFlow<Boolean>()
    private val _startButtonPressed = MutableSharedFlow<Unit>()

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState

    fun initialize() {
        viewModelScope.launch {
            interactor.start(this@MainViewModel)
        }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.stop()
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.CloseDoor -> {
                setState { doorOpen = false }
                viewModelScope.launch {
                    _doorStatusChanged.emit(uiState.value.doorOpen)
                }
            }

            MainEvent.OpenDoor -> {
                setState { doorOpen = true }
                viewModelScope.launch {
                    _doorStatusChanged.emit(uiState.value.doorOpen)
                }
            }

            MainEvent.StartMicroWave -> _startButtonPressed.tryEmit(Unit)
        }
    }

    override fun turnOnHeater() = setState { heaterOn = true }

    override fun turnOffHeater() = setState { heaterOn = false }

    override fun isDoorOpen(): Boolean = uiState.value.doorOpen

    override val doorStatusChanged: SharedFlow<Boolean> get() = _doorStatusChanged

    override val startButtonPressed: SharedFlow<Unit> get() = _startButtonPressed

    private fun setState(func: MainUIState.Builder.() -> Unit) {
        _uiState.value = _uiState.value.build(func)
    }
}