package com.app.screentime.calling.di

import com.app.screentime.calling.data.repository.CallRepositoryImpl
import com.app.screentime.calling.domain.repository.CallRepository
import com.app.screentime.calling.domain.usecase.BillingTickHandler
import com.app.screentime.calling.domain.usecase.CallUseCase
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CallingModule {

    @Binds
    @Singleton
    abstract fun bindCallRepository(
        impl: CallRepositoryImpl
    ): CallRepository

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(networkClient: NetworkClient): HttpClient {
            return networkClient.httpClient
        }

        @Provides
        @Singleton
        fun provideBillingTickHandler(): BillingTickHandler {
            return BillingTickHandler()
        }

        @Provides
        @Singleton
        fun provideCallUseCase(
            repository: CallRepository,
            billingHandler: BillingTickHandler,
            api: ChattyApi,
            sessionManager: SessionManager
        ): CallUseCase {
            return CallUseCase(repository, billingHandler, api, sessionManager)
        }
    }
}
