package com.app.screentime.di

import android.content.Context
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.dao.BlockedLinkDao
import com.app.screentime.database.dao.FocusTimeDao
import com.app.screentime.database.dao.JoinedChallengeDao
import com.app.screentime.database.dao.NotificationDao
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

    @Provides
    fun provideFocusTimeDao(database: ScreenTimeDatabase): FocusTimeDao {
        return database.focusTimeDao()
    }
    
    // BlockedLinkDao kept for future use - currently not providing it to avoid dependency issues
    // @Provides
    // fun provideBlockedLinkDao(database: ScreenTimeDatabase): BlockedLinkDao {
    //     return database.blockedLinkDao()
    // }
    
    @Provides
    fun provideNotificationDao(database: ScreenTimeDatabase): NotificationDao {
        return database.notificationDao()
    }
    
    @Provides
    fun provideJoinedChallengeDao(database: ScreenTimeDatabase): JoinedChallengeDao {
        return database.joinedChallengeDao()
    }
}
