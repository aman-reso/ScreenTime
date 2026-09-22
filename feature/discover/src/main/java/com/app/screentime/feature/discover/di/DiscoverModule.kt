package com.app.screentime.feature.discover.di

import com.app.screentime.feature.discover.data.repository.DiscoverRepositoryImpl
import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Hilt dependency injection module binding DiscoverRepository abstraction to DiscoverRepositoryImpl.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DiscoverModule {

    @Binds
    abstract fun bindDiscoverRepository(
        impl: DiscoverRepositoryImpl
    ): DiscoverRepository
}
