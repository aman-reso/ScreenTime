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
    fun provideAppConfigManager(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager
    ): com.app.screentime.core.network.config.AppConfigManager {
        return com.app.screentime.core.network.config.AppConfigManager(context, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideFirebaseConfigManager(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager,
        appConfigManager: com.app.screentime.core.network.config.AppConfigManager
    ): com.app.screentime.core.network.config.FirebaseConfigManager {
        return com.app.screentime.core.network.config.FirebaseConfigManager(context, preferencesManager, appConfigManager)
    }

    @Provides
    @Singleton
    fun providePlayIntegrityManager(
        @ApplicationContext context: Context
    ): com.app.screentime.core.network.security.PlayIntegrityManager {
        return com.app.screentime.core.network.security.PlayIntegrityManager(context)
    }

    @Provides
    @Singleton
    fun provideNetworkClient(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager,
        firebaseConfigManager: com.app.screentime.core.network.config.FirebaseConfigManager,
        playIntegrityManager: com.app.screentime.core.network.security.PlayIntegrityManager
    ): NetworkClient = NetworkClient(context, preferencesManager, firebaseConfigManager, playIntegrityManager)

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context,
        preferencesManager: PreferencesManager,
        e2eCryptoManagerProvider: javax.inject.Provider<com.app.screentime.core.network.security.E2ECryptoManager>
    ): SessionManager = SessionManager(context, preferencesManager, e2eCryptoManagerProvider)

    @Provides
    @Singleton
    fun provideWinterApi(
        networkClient: NetworkClient
    ): WinterApi = WinterApi(networkClient)


    @Provides
    @Singleton
    fun provideWinterWebSocketClient(
        api: WinterApi,
        sessionManager: SessionManager,
        firebaseConfigManager: com.app.screentime.core.network.config.FirebaseConfigManager
    ): WinterWebSocketClient = WinterWebSocketClient(api, sessionManager, firebaseConfigManager)

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

