package com.app.screentime.network.di

import com.app.screentime.core.network.NetworkClient
import com.app.screentime.login.service.LoginService
import com.app.screentime.login.service.LoginServiceImpl
import com.app.screentime.consent.service.ConsentService
import com.app.screentime.consent.service.ConsentServiceImpl
import com.app.screentime.challenge.service.ChallengeService
import com.app.screentime.challenge.service.ChallengeServiceImpl
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
import com.app.screentime.reward.service.RewardService
import com.app.screentime.reward.service.RewardServiceImpl
import com.app.screentime.network.service.user.UserService
import com.app.screentime.network.service.user.UserServiceImpl
import com.app.screentime.search.service.SearchService
import com.app.screentime.search.service.SearchServiceImpl
import com.app.screentime.controlcenter.service.ControlCenterService
import com.app.screentime.controlcenter.service.ControlCenterServiceImpl
import com.app.screentime.location.service.LocationService
import com.app.screentime.location.service.LocationServiceImpl
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
     * Bind UserService implementation
     */
    @Binds
    @Singleton
    abstract fun bindUserService(userServiceImpl: UserServiceImpl): UserService

    /**
     * Bind SearchService implementation
     */
    @Binds
    abstract fun bindSearchService(searchServiceImpl: SearchServiceImpl): SearchService

    /**
     * Bind ProfileService implementation
     */
    @Binds
    abstract fun bindProfileService(profileServiceImpl: ProfileServiceImpl): ProfileService

    /**
     * Bind TOTPService implementation (network package)
     */
    @Binds
    abstract fun bindTOTPService(totpServiceImpl: TOTPServiceImpl): TOTPService

    /**
     * Bind Profile TOTPService implementation (profile package)
     */
    @Binds
    abstract fun bindProfileTOTPService(profileTotpServiceImpl: ProfileTOTPServiceImpl): ProfileTOTPService

    /**
     * Bind ConsentService implementation
     */
    @Binds
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
    abstract fun bindNotificationService(notificationServiceImpl: NotificationServiceImpl): NotificationService

    /**
     * Bind LeaderboardService implementation
     */
    @Binds
    abstract fun bindLeaderboardService(leaderboardServiceImpl: LeaderboardServiceImpl): LeaderboardService

    /**
     * Bind ChallengeService implementation
     */
    @Binds
    abstract fun bindChallengeService(challengeServiceImpl: ChallengeServiceImpl): ChallengeService

    /**
     * Bind FocusService implementation
     */
    @Binds
    abstract fun bindFocusService(focusServiceImpl: FocusServiceImpl): FocusService

    /**
     * Bind BlockedDomainService implementation
     */
    @Binds
    abstract fun bindBlockedDomainService(blockedDomainServiceImpl: BlockedDomainServiceImpl): BlockedDomainService

    /**
     * Bind URLSearchService implementation
     */
    @Binds
    abstract fun bindURLSearchService(urlSearchServiceImpl: URLSearchServiceImpl): URLSearchService

    /**
     * Bind RewardService implementation
     */
    @Binds
    abstract fun bindRewardService(rewardServiceImpl: RewardServiceImpl): RewardService

    /**
     * Bind ControlCenterService implementation
     */
    @Binds
    abstract fun bindControlCenterService(controlCenterServiceImpl: ControlCenterServiceImpl): ControlCenterService

    /**
     * Bind LocationService implementation
     */
    @Binds
    abstract fun bindLocationService(locationServiceImpl: LocationServiceImpl): LocationService

    companion object {
        /**
         * Provide NetworkUtils
         * Uses @Provides because it needs Context parameter
         */
    }
}
