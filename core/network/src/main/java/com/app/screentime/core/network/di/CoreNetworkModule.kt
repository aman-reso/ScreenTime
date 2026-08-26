package com.app.screentime.core.network.di

import android.content.Context
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
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
        @ApplicationContext context: Context
    ): SessionManager = SessionManager(context)

    @Provides
    @Singleton
    fun provideChattyApi(
        networkClient: NetworkClient
    ): ChattyApi = ChattyApi(networkClient)

    @Provides
    @Singleton
    fun provideChattyWebSocketClient(
        api: ChattyApi,
        sessionManager: SessionManager
    ): ChattyWebSocketClient = ChattyWebSocketClient(api, sessionManager)
}
