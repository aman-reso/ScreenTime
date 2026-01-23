package com.app.screentime.config.di

import android.content.Context
import com.app.screentime.config.ConfigManager
import com.app.screentime.config.ConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideConfigRepository(
        @ApplicationContext context: Context,
        configService: com.app.screentime.core.network.service.ConfigService,
        preferencesManager: com.app.screentime.core.network.preferences.PreferencesManager
    ): ConfigRepository {
        return ConfigRepository(context, configService, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideConfigManager(
        configRepository: ConfigRepository
    ): ConfigManager {
        return ConfigManager(configRepository)
    }
}

