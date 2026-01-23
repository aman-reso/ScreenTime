package com.app.screentime.core.network.di

import android.content.Context
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideNetworkClient(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager
    ): NetworkClient = NetworkClient(context, preferencesManager)

    @Provides
    @Singleton
    fun provideWallpaperService(networkClient: NetworkClient): com.app.screentime.core.network.service.WallpaperService =
        com.app.screentime.core.network.service.WallpaperServiceImpl(networkClient)

    @Provides
    @Singleton
    fun provideConfigService(networkClient: NetworkClient): com.app.screentime.core.network.service.ConfigService =
        com.app.screentime.core.network.service.ConfigServiceImpl(networkClient)

    @Provides
    @Singleton
    fun provideFeedbackService(networkClient: NetworkClient): com.app.screentime.core.network.service.FeedbackService =
        com.app.screentime.core.network.service.FeedbackServiceImpl(networkClient)
}
