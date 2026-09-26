package com.app.screentime.core.network.di

import android.content.Context
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.api.DiscoveryApi
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WinterWebSocketClient
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
    fun provideSessionManager(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager
    ): SessionManager = SessionManager(context, preferencesManager)

    @Provides
    @Singleton
    fun provideWinterApi(
        networkClient: NetworkClient
    ): WinterApi = WinterApi(networkClient)


    @Provides
    @Singleton
    fun provideWinterWebSocketClient(
        api: WinterApi,
        sessionManager: SessionManager
    ): WinterWebSocketClient = WinterWebSocketClient(api, sessionManager)

    @Provides
    @Singleton
    fun provideDiscoveryApi(networkClient: NetworkClient): DiscoveryApi = DiscoveryApi(networkClient.httpClient)

    @Provides
    @Singleton
    fun provideMessagesApi(networkClient: NetworkClient): com.app.screentime.core.network.api.MessagesApi =
        com.app.screentime.core.network.api.MessagesApi(networkClient.httpClient)

    @Provides
    @Singleton
    fun provideFcmTokenManager(
        @ApplicationContext context: Context,
        winterApi: WinterApi,
        preferencesManager: PreferencesManager
    ): com.app.screentime.core.network.session.FcmTokenManager =
        com.app.screentime.core.network.session.FcmTokenManager(context, winterApi, preferencesManager)
}

