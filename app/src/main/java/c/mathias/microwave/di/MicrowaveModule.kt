package c.mathias.microwave.di

import c.mathias.microwave.controller.MicrowaveController
import c.mathias.microwave.controller.MicrowaveControllerImpl
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
    fun provideMicrowaveController(): MicrowaveController = MicrowaveControllerImpl()

}