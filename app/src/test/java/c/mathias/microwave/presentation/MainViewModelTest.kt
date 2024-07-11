package c.mathias.microwave.presentation

import c.mathias.microwave.manager.MicrowaveManager
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @RelaxedMockK
    private lateinit var interactor: MicrowaveManager

    @BeforeEach
    fun setUp() {
        viewModel = MainViewModel(interactor)
    }

    @Test
    fun `initialize calls start on interactor`() {
        viewModel.initialize()

        verify { interactor.start(viewModel) }
    }

    @Test
    fun `light is on when the microwave door is open and off when the door is closed`() {
        viewModel.handleEvent(MainEvent.OpenDoor)
        var uiState = viewModel.uiState.value
        assertTrue(uiState.doorOpen)

        viewModel.handleEvent(MainEvent.CloseDoor)
        uiState = viewModel.uiState.value
        assertFalse(uiState.doorOpen)
    }

}