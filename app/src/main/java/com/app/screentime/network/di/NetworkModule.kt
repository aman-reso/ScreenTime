package com.app.screentime.network.di

import com.app.screentime.network.NetworkClient
import com.app.screentime.network.repository.NetworkRepository
import com.app.screentime.network.service.ApiService
import com.app.screentime.network.service.ApiServiceImpl
import com.app.screentime.network.utils.NetworkUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for network dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    /**
     * Bind ApiService implementation
     */
    @Binds
    @Singleton
    abstract fun bindApiService(apiServiceImpl: ApiServiceImpl): ApiService

    companion object {
        /**
         * Provide NetworkClient
         */
        @Provides
        @Singleton
        fun provideNetworkClient(): NetworkClient = NetworkClient()

        /**
         * Provide NetworkRepository
         */

        /**
         * Provide NetworkUtils
         */
        @Provides
        @Singleton
        fun provideNetworkUtils(context: android.content.Context): NetworkUtils {
            return NetworkUtils(context)
        }

    }
}
