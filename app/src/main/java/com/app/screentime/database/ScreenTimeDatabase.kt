package com.app.screentime.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
// FocusTimeDao removed - server is source of truth
// JoinedChallengeDao removed - server is source of truth
import com.app.screentime.database.dao.CapturedNotificationDao
import com.app.screentime.database.entity.CapturedNotificationEntity
// FocusTimeEntity removed - server is source of truth
// JoinedChallengeEntity removed - server is source of truth

/**
 * Room database for ScreenTime app
 * 
 * MIGRATION NOTE: FocusTimeEntity and JoinedChallengeEntity have been removed.
 * Server is now the single source of truth for focus time and joined challenges.
 * All repository operations are no-op for migration safety.
 */
@Database(
    entities = [
        CapturedNotificationEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class ScreenTimeDatabase : RoomDatabase() {
    
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

