package com.app.screentime.network.di

import com.app.screentime.consent.service.ConsentService
import com.app.screentime.consent.service.ConsentServiceImpl
import com.app.screentime.login.service.LoginService
import com.app.screentime.login.service.LoginServiceImpl
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.service.notification.NotificationService
import com.app.screentime.network.service.notification.NotificationServiceImpl
import com.app.screentime.network.service.screentime.ScreenTimeService
import com.app.screentime.network.service.screentime.ScreenTimeServiceImpl
import com.app.screentime.leaderboard.service.LeaderboardService
import com.app.screentime.leaderboard.service.LeaderboardServiceImpl
import com.app.screentime.network.service.blockeddomain.BlockedDomainService
import com.app.screentime.network.service.blockeddomain.BlockedDomainServiceImpl
import com.app.screentime.network.service.focus.FocusService
import com.app.screentime.network.service.focus.FocusServiceImpl
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.profile.service.ProfileService
import com.app.screentime.profile.service.ProfileServiceImpl
import com.app.screentime.profile.service.TOTPService as ProfileTOTPService
import com.app.screentime.profile.service.TOTPServiceImpl as ProfileTOTPServiceImpl
import com.app.screentime.network.service.totp.TOTPService
import com.app.screentime.network.service.totp.TOTPServiceImpl
import com.app.screentime.network.service.urlsearch.URLSearchService
import com.app.screentime.network.service.urlsearch.URLSearchServiceImpl
import com.app.screentime.search.service.SearchService
import com.app.screentime.search.service.SearchServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for network dependencies
 * Provides all API service interfaces and their implementations
 * 
 * DI Pattern:
 * - @Binds: Used for binding interfaces to implementations when implementation has @Inject constructor
 *   (More efficient, compile-time binding, preferred approach)
 * - @Provides: Used only when custom instantiation logic is needed or parameters can't be auto-injected
 *   (e.g., Context, custom factory methods)
 * 
 * All service implementations use @Binds since they have @Inject constructor with NetworkClient dependency.
 * Only NetworkClient and NetworkUtils use @Provides (NetworkClient could use @Binds too, but @Provides is kept for clarity).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    /**
     * Bind LoginService implementation
     */
    @Binds
    @Singleton
    abstract fun bindLoginService(loginServiceImpl: LoginServiceImpl): LoginService

    /**
     * Bind SearchService implementation
     */
    @Binds
    @Singleton
    abstract fun bindSearchService(searchServiceImpl: SearchServiceImpl): SearchService

    /**
     * Bind ProfileService implementation
     */
    @Binds
    @Singleton
    abstract fun bindProfileService(profileServiceImpl: ProfileServiceImpl): ProfileService

    /**
     * Bind TOTPService implementation (network package)
     */
    @Binds
    @Singleton
    abstract fun bindTOTPService(totpServiceImpl: TOTPServiceImpl): TOTPService

    /**
     * Bind Profile TOTPService implementation (profile package)
     */
    @Binds
    @Singleton
    abstract fun bindProfileTOTPService(profileTotpServiceImpl: ProfileTOTPServiceImpl): ProfileTOTPService

    /**
     * Bind ConsentService implementation
     */
    @Binds
    @Singleton
    abstract fun bindConsentService(consentServiceImpl: ConsentServiceImpl): ConsentService

    /**
     * Bind ScreenTimeService implementation
     */
    @Binds
    @Singleton
    abstract fun bindScreenTimeService(screenTimeServiceImpl: ScreenTimeServiceImpl): ScreenTimeService

    /**
     * Bind NotificationService implementation
     */
    @Binds
    @Singleton
    abstract fun bindNotificationService(notificationServiceImpl: NotificationServiceImpl): NotificationService

    /**
     * Bind LeaderboardService implementation
     */
    @Binds
    @Singleton
    abstract fun bindLeaderboardService(leaderboardServiceImpl: LeaderboardServiceImpl): LeaderboardService

    /**
     * Bind FocusService implementation
     */
    @Binds
    @Singleton
    abstract fun bindFocusService(focusServiceImpl: FocusServiceImpl): FocusService

    /**
     * Bind BlockedDomainService implementation
     */
    @Binds
    @Singleton
    abstract fun bindBlockedDomainService(blockedDomainServiceImpl: BlockedDomainServiceImpl): BlockedDomainService

    /**
     * Bind URLSearchService implementation
     */
    @Binds
    @Singleton
    abstract fun bindURLSearchService(urlSearchServiceImpl: URLSearchServiceImpl): URLSearchService

    companion object {
        /**
         * Provide NetworkClient
         * Uses @Provides because NetworkClient is a concrete class that needs instantiation
         * Creates PreferencesManager directly instead of injecting it
         */
        @Provides
        @Singleton
        fun provideNetworkClient(
            @ApplicationContext context: android.content.Context
        ): NetworkClient = NetworkClient(context)

        /**
         * Provide NetworkUtils
         * Uses @Provides because it needs Context parameter
         */
        @Provides
        @Singleton
        fun provideNetworkUtils(context: android.content.Context): NetworkUtils {
            return NetworkUtils(context)
        }
    }
}
