package com.app.screentime.feature.chat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE partnerId = :partnerId ORDER BY timestamp ASC")
    fun getMessagesFlow(partnerId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE partnerId = :partnerId ORDER BY timestamp ASC")
    suspend fun getMessages(partnerId: String): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_messages WHERE partnerId = :partnerId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessage(partnerId: String): ChatMessageEntity?

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllMessagesFlow(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chat_messages WHERE id = :id")
    suspend fun deleteMessage(id: String)

    @Query("DELETE FROM chat_messages WHERE partnerId = :partnerId")
    suspend fun clearMessagesForPartner(partnerId: String)
}
