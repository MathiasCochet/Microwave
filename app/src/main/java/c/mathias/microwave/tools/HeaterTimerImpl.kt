package c.mathias.microwave.tools

import android.os.CountDownTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeaterTimerImpl : HeaterTimer {
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0L


    override suspend fun startTimer(timeLeft: Long, onFinish: () -> Unit) {
        withContext(Dispatchers.Main) {
            remainingTime = timeLeft

            timer = object : CountDownTimer(timeLeft, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                }

                override fun onFinish() {
                    remainingTime = 0L
                    onFinish()
                }
            }.start()
        }
    }

    override suspend fun increaseTime(onFinish: () -> Unit) {
        stop()
        startTimer(remainingTime + HeaterTimer.HEATER_TIME, onFinish)
    }

    override fun finished(): Boolean {
        return remainingTime == 0L
    }

    override fun stop() {
        timer?.cancel()
        remainingTime = 0L
    }
}