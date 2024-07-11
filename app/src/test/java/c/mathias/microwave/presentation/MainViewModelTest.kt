package c.mathias.microwave.presentation

import app.cash.turbine.test
import c.mathias.microwave.controller.MicrowaveDoorState
import c.mathias.microwave.manager.MicrowaveManager
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @RelaxedMockK
    private lateinit var microwaveManager: MicrowaveManager

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = MainViewModel(microwaveManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialize calls starts manager`() = runTest {
        viewModel.initialize()

        coVerify { microwaveManager.start(viewModel) }
    }

    @Test
    fun `check door status`() = runTest {
        viewModel.uiState.test {
            assertFalse(awaitItem().doorOpen)

            viewModel.handleEvent(MainEvent.OpenDoor)
            assertTrue(awaitItem().doorOpen)

            viewModel.handleEvent(MainEvent.CloseDoor)
            assertFalse(awaitItem().doorOpen)
        }
    }

    @Test
    fun `emit door status when door opens`() = runTest {
        viewModel.doorStatusChanged.test {
            viewModel.handleEvent(MainEvent.OpenDoor)
            assertEquals(MicrowaveDoorState.Open, awaitItem())
        }
    }

    @Test
    fun `emit door status when door closes`() = runTest {
        viewModel.doorStatusChanged.test {
            viewModel.handleEvent(MainEvent.CloseDoor)
            assertEquals(MicrowaveDoorState.Closed, awaitItem())
        }
    }

    @Test
    fun `emit start button pressed`() = runTest {
        viewModel.startButtonPressed.test {
            viewModel.handleEvent(MainEvent.StartMicroWave)
            assertEquals(Unit, awaitItem())
        }
    }

    @Test
    fun `turning on & off the heater`() = runTest {
        viewModel.uiState.test {
            assertFalse(awaitItem().heaterOn)

            viewModel.turnOnHeater()
            assertTrue(awaitItem().heaterOn)

            viewModel.turnOffHeater()
            assertFalse(awaitItem().heaterOn)
        }
    }

    @Test
    fun `is door open`() = runTest {
        assertFalse(viewModel.isDoorOpen())

        viewModel.handleEvent(MainEvent.OpenDoor)
        assertTrue(viewModel.isDoorOpen())

        viewModel.handleEvent(MainEvent.CloseDoor)
        assertFalse(viewModel.isDoorOpen())
    }

}