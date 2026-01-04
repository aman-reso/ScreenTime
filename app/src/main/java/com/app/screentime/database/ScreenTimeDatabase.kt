package com.app.screentime.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.app.screentime.database.dao.BlockedLinkDao
import com.app.screentime.database.dao.FocusTimeDao
import com.app.screentime.database.dao.JoinedChallengeDao
import com.app.screentime.database.dao.CapturedNotificationDao
import com.app.screentime.database.dao.NotificationDao
import com.app.screentime.database.entity.BlockedLinkEntity
import com.app.screentime.database.entity.CapturedNotificationEntity
import com.app.screentime.database.entity.FocusTimeEntity
import com.app.screentime.database.entity.JoinedChallengeEntity
import com.app.screentime.database.entity.NotificationEntity

/**
 * Room database for ScreenTime app
 */
@Database(
    entities = [
        FocusTimeEntity::class,
        BlockedLinkEntity::class, // Kept for future use - currently not in use
        NotificationEntity::class,
        JoinedChallengeEntity::class,
        CapturedNotificationEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class ScreenTimeDatabase : RoomDatabase() {
    
    abstract fun focusTimeDao(): FocusTimeDao
    abstract fun blockedLinkDao(): BlockedLinkDao // Kept for future use - currently not in use
    abstract fun notificationDao(): NotificationDao
    abstract fun joinedChallengeDao(): JoinedChallengeDao
    abstract fun capturedNotificationDao(): CapturedNotificationDao
    
    companion object {
        private const val DATABASE_NAME = "screentime_database"
        
        @Volatile
        private var INSTANCE: ScreenTimeDatabase? = null
        
        fun getDatabase(context: Context): ScreenTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScreenTimeDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration(true) // Allow destructive migration since app is not published yet
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Type converters for Room (if needed in the future)
 */
class Converters {
    // Add type converters here if needed
}

