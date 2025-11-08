package com.app.screentime.di

import android.content.Context
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.ui.language.LanguageRepository
import com.app.screentime.ui.theme.ThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkUsageHelper(@ApplicationContext context: Context): NetworkUsageHelper {
        return NetworkUsageHelper(context)
    }


    @Provides
    @Singleton
    fun provideScreenUsageHelper(@ApplicationContext context: Context): ScreenUsageHelper {
        return ScreenUsageHelper(context)
    }


    @Provides
    @Singleton
    fun provideLocalAppUsageRepository(
        @ApplicationContext context: Context,
        screenUsageHelper: ScreenUsageHelper,
        networkUsageHelper: NetworkUsageHelper
    ): LocalAppUsageRepository {
        return LocalAppUsageRepository(context, screenUsageHelper, networkUsageHelper)
    }

}