package c.mathias.microwave.di

import c.mathias.microwave.manager.MicrowaveManager
import c.mathias.microwave.manager.MicrowaveManagerImpl
import c.mathias.microwave.tools.HeaterTimer
import c.mathias.microwave.tools.HeaterTimerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MicrowaveModule {

    @Provides
    @Singleton
    fun provideHeaterTimer(): HeaterTimer = HeaterTimerImpl()

    @Provides
    @Singleton
    fun provideMicrowaveInteractor(heaterTimer: HeaterTimer): MicrowaveManager =
        MicrowaveManagerImpl(heaterTimer)
}