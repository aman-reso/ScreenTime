package com.app.screentime.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
//    @Provides
//    @Singleton
//    fun provideScreenTimeDatabase(
//        @ApplicationContext context: Context
//    ): ScreenTimeDatabase {
//        return Room.databaseBuilder(
//            context,
//            ScreenTimeDatabase::class.java,
//            ScreenTimeDatabase.DATABASE_NAME
//        ).build()
//    }
//
//    @Provides
//    fun provideAppUsageDao(database: ScreenTimeDatabase): AppUsageDao {
//        return database.appUsageDao()
//    }
}
