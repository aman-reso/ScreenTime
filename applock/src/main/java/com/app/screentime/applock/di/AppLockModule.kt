package com.app.screentime.applock.di

import android.content.Context
import com.app.screentime.applock.repository.AppLockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppLockModule {

    @Provides
    @Singleton
    fun provideAppLockRepository(
        @ApplicationContext context: Context
    ): AppLockRepository {
        return AppLockRepository(context)
    }
}

