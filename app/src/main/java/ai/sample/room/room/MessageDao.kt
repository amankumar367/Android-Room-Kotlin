package ai.sample.room.room

import ai.sample.room.data.MediaMessage
import ai.sample.room.data.Message
import androidx.room.*

@Dao
interface MessageDao {
    @Insert
    fun insertMessage(person: Message?)

    @Insert
    fun insertMultipleMessage(person: List<Message>?)

    @Insert
    fun insertMultipleMediaMessage(person: List<MediaMessage>?)

    @Update
    fun updateMessage(person: Message?)

    @Delete
    fun delete(person: Message?)

    @Query("SELECT * FROM MESSAGE ORDER BY messageId")
    fun loadAllMessage(): List<Message?>?

    @Query("SELECT * FROM MEDIAMESSAGE ORDER BY mediaMessageId")
    fun loadAllMediaMessage(): List<MediaMessage?>?

    @Query("DELETE FROM MESSAGE")
    fun deleteAllMessages()

    @Query("SELECT * FROM MESSAGE WHERE MESSAGEID = :id")
    fun loadMessageById(id: Int): Message?
}
