package c.mathias.microwave.di

import c.mathias.microwave.manager.MicrowaveManager
import c.mathias.microwave.manager.MicrowaveManagerImpl
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
    fun provideMicrowaveInteractor(): MicrowaveManager = MicrowaveManagerImpl()

}