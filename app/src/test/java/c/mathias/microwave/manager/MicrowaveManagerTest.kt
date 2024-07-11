package c.mathias.microwave.manager

import c.mathias.microwave.controller.MicrowaveController
import c.mathias.microwave.controller.MicrowaveDoorState
import c.mathias.microwave.tools.HeaterTimer
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class MicrowaveManagerTest {

    private lateinit var microwaveManager: MicrowaveManagerImpl

    @RelaxedMockK
    private lateinit var heaterTimer: HeaterTimer

    @RelaxedMockK
    private lateinit var microwaveController: MicrowaveController

    private val _doorStatusChanged = MutableSharedFlow<MicrowaveDoorState>()
    private val _startButtonPressed = MutableSharedFlow<Unit>()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        microwaveManager = MicrowaveManagerImpl(heaterTimer, Dispatchers.Main)

        coEvery { microwaveController.doorStatusChanged } returns _doorStatusChanged
        coEvery { microwaveController.startButtonPressed } returns _startButtonPressed
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `heater timer starts when start button is pressed and door is closed`() = runTest {
        val callbackSlot = slot<() -> Unit>()

        coEvery { microwaveController.isDoorOpen() } returns false
        coEvery { heaterTimer.finished() } returns true
        coEvery { heaterTimer.startTimer(any(), capture(callbackSlot)) } just Runs

        microwaveManager.start(microwaveController)

        _startButtonPressed.emit(Unit)
        callbackSlot.captured()

        coVerify { microwaveController.turnOnHeater() }
        coVerify { heaterTimer.startTimer(any(), any()) }
        coVerify { microwaveController.turnOffHeater() }
    }

    @Test
    fun `heater timer increases when I press start again while heater is on`() = runTest {
        val callbackSlot = slot<() -> Unit>()

        coEvery { microwaveController.isDoorOpen() } returns false
        coEvery { heaterTimer.finished() } returns false
        coEvery { heaterTimer.increaseTime(capture(callbackSlot)) } just Runs

        microwaveManager.start(microwaveController)

        _startButtonPressed.emit(Unit)
        callbackSlot.captured()

        coVerify { heaterTimer.increaseTime(any()) }
        coVerify { microwaveController.turnOffHeater() }
    }

    @Test
    fun `heater does not turn on when start button is pressed and door is open`() = runTest {
        coEvery { microwaveController.isDoorOpen() } returns true

        microwaveManager.start(microwaveController)

        _startButtonPressed.emit(Unit)

        coVerify(exactly = 0) { microwaveController.turnOnHeater() }
    }

    @Test
    fun `heater turns off when door is opened`() = runTest {
        coEvery { microwaveController.isDoorOpen() } returns false
        coEvery { heaterTimer.finished() } returns false
        coEvery { heaterTimer.startTimer(any(), any()) } just Runs

        microwaveManager.start(microwaveController)

        _doorStatusChanged.emit(MicrowaveDoorState.Open)

        coVerify { microwaveController.turnOffHeater() }
        coVerify { heaterTimer.stop() }
    }

    @Test
    fun `closing the door doesn't trigger anything`() = runTest {
        coEvery { microwaveController.isDoorOpen() } returns true
        coEvery { heaterTimer.startTimer(any(), any()) } just Runs

        microwaveManager.start(microwaveController)

        _doorStatusChanged.emit(MicrowaveDoorState.Closed)

        coVerify(exactly = 0) { microwaveController.turnOffHeater() }
        coVerify(exactly = 0) { heaterTimer.stop() }
    }
}