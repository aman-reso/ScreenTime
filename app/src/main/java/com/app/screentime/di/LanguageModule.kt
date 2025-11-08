package com.app.screentime.di

import android.content.Context
import com.app.screentime.ui.language.LanguageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LanguageModule {

    @Provides
    @Singleton
    fun provideLanguageRepository(@ApplicationContext context: Context): LanguageRepository {
        return LanguageRepository(context)
    }
}
