package com.app.screentime.di

import android.content.Context
import com.app.screentime.database.ScreenTimeDatabase
// FocusTimeDao removed - server is source of truth
// JoinedChallengeDao removed - server is source of truth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideScreenTimeDatabase(
        @ApplicationContext context: Context
    ): ScreenTimeDatabase {
        return ScreenTimeDatabase.getDatabase(context)
    }
}
