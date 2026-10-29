package com.app.screentime.feature.preferences.di

import com.app.screentime.feature.preferences.data.repository.EditPreferencesRepositoryImpl
import com.app.screentime.feature.preferences.domain.repository.EditPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EditPreferencesModule {

    @Binds
    @Singleton
    abstract fun bindEditPreferencesRepository(
        impl: EditPreferencesRepositoryImpl
    ): EditPreferencesRepository
}
