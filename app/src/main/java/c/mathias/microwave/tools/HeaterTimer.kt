package c.mathias.microwave.tools

interface HeaterTimer {

    companion object {
        const val HEATER_TIME = 60000L
    }

    suspend fun startTimer(timeLeft: Long = HEATER_TIME, onFinish: () -> Unit)

    suspend fun increaseTime(onFinish: () -> Unit)

    fun finished(): Boolean

    fun stop()
}