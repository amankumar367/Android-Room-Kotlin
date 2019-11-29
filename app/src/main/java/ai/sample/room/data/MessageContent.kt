package ai.sample.room.data


import com.google.gson.annotations.SerializedName

data class MessageContent(
    @SerializedName("messages")
    val messages: List<Messages?>?
) {
    data class Messages(
        @SerializedName("mediaMessages")
        val mediaMessages: MediaMessages?,
        @SerializedName("messageId")
        val messageId: Int?,
        @SerializedName("messageType")
        val messageType: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("text")
        val text: String?,
        @SerializedName("time")
        val time: Int?
    ) {
        data class MediaMessages(
            @SerializedName("mediaMessageId")
            val mediaMessageId: Int?,
            @SerializedName("subType")
            val subType: String?,
            @SerializedName("thumbnails")
            val thumbnails: String?,
            @SerializedName("type")
            val type: String?
        )
    }
}