package c.mathias.microwave.presentation

import androidx.lifecycle.ViewModel
import c.mathias.microwave.controller.MicrowaveController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val microwaveController: MicrowaveController
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState

    fun updateDoorStatus() {
        setState {
            doorOpen = !doorOpen
            lightOn = doorOpen
        }
    }

    private fun setState(func: MainUIState.Builder.() -> Unit) {
        _uiState.value = _uiState.value.build(func)
    }
}