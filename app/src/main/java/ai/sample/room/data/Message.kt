package ai.sample.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message (
    @PrimaryKey val messageId: Int,
    @ColumnInfo(name = "messageType") var messageType: String?,
    @ColumnInfo(name = "text") var text: String?,
    @ColumnInfo(name = "time") var time: Int?,
    @ColumnInfo(name = "status") var status: String?
)