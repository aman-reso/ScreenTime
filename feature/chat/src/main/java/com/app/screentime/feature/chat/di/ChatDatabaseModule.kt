package com.app.screentime.feature.chat.di

import android.content.Context
import androidx.room.Room
import com.app.screentime.feature.chat.data.local.ChatDao
import com.app.screentime.feature.chat.data.local.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDatabaseModule {

    @Provides
    @Singleton
    fun provideChatDatabase(
        @ApplicationContext context: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideChatDao(database: ChatDatabase): ChatDao {
        return database.chatDao()
    }
}
