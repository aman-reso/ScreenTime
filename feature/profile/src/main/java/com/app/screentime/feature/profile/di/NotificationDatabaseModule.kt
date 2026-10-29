package com.app.screentime.feature.profile.di

import android.content.Context
import androidx.room.Room
import com.app.screentime.feature.profile.data.local.NotificationDao
import com.app.screentime.feature.profile.data.local.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationDatabaseModule {

    @Provides
    @Singleton
    fun provideNotificationDatabase(
        @ApplicationContext context: Context
    ): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "winter_notifications.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(
        database: NotificationDatabase
    ): NotificationDao {
        return database.notificationDao()
    }
}
