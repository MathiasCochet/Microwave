package c.mathias.microwave.manager

import c.mathias.microwave.presentation.MicrowaveController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
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
    private lateinit var microwaveController: MicrowaveController

    private val _startButtonPressed = MutableSharedFlow<Unit>()
    private val _doorStatusChanged = MutableSharedFlow<Boolean>()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        microwaveManager = MicrowaveManagerImpl(Dispatchers.Main)

        coEvery { microwaveController.startButtonPressed } returns _startButtonPressed
        coEvery { microwaveController.doorStatusChanged } returns _doorStatusChanged
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `heater turns on when start button is pressed and door is closed`() = runTest {
        coEvery { microwaveController.isDoorOpen() } returns false

        microwaveManager.start(microwaveController)

        _startButtonPressed.emit(Unit)

        coVerify { microwaveController.turnOnHeater() }
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
        microwaveManager.start(microwaveController)

        _doorStatusChanged.emit(false)

        coVerify { microwaveController.turnOffHeater() }
    }

    @Test
    fun `heater doesn't turn on when door is closed`() = runTest {
        microwaveManager.start(microwaveController)

        _doorStatusChanged.emit(true)

        coVerify(exactly = 0) { microwaveController.turnOnHeater() }
    }
}