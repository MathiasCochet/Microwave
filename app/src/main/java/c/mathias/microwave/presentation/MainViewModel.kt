package c.mathias.microwave.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {

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