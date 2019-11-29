package ai.sample.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "mediaMessage",
    foreignKeys = [ForeignKey(entity = Message::class, parentColumns = ["messageId"], childColumns = ["mediaMessageId"], onDelete = CASCADE)])
data class MediaMessage (
    @PrimaryKey val mediaMessageId: Int,
    @ColumnInfo(name = "messageId") val messageId: Int,
    @ColumnInfo(name = "thumbnails") var thumbnails: String?,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "subType") var subType: String?
)