package ai.sample.room

import ai.sample.room.data.MediaMessage
import ai.sample.room.data.Message
import ai.sample.room.data.MessageContent
import ai.sample.room.room.AppDatabase
import ai.sample.room.utils.MESSAGE_FILE_NAME
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.util.concurrent.Executor

class DataInitializer(
    context: Context,
    private val executor: Executor,
    private val db: AppDatabase,
    private val dbcallback: DatabaseCallback) {

    init {
        getDataFromAssestFile(context.applicationContext)
    }

    private fun getDataFromAssestFile(context: Context) {
        try {
            context.assets.open(MESSAGE_FILE_NAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val messageContentType = object : TypeToken<MessageContent>() {}.type
                    val messageContent: MessageContent = Gson().fromJson(jsonReader, messageContentType)

                    populateSampleData(messageContent)
                    Log.d(TAG, " >>> Contents of Message JSON  ===> $messageContent")
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Error on reading Message file", exception)
            dbcallback.failure(exception.localizedMessage)
        }
    }

    private fun populateSampleData(messageContent: MessageContent) {
        val messageList = mutableListOf<Message>()
        val mediaMessagesList = mutableListOf<MediaMessage>()
        messageContent.messages?.let{
            for (message in it) {
                message?.let { messages ->
                    messageList += Message(
                        messageId = messages.messageId!!,
                        messageType = messages.messageType,
                        text = messages.text,
                        time = messages.time,
                        status = messages.status)

                    if(messages.messageType != "text") {
                        mediaMessagesList += MediaMessage(
                            mediaMessageId = messages.mediaMessages!!.mediaMessageId!!,
                            messageId = messages.messageId!!,
                            thumbnails = messages.mediaMessages.thumbnails,
                            type = messages.mediaMessages.type,
                            subType = messages.mediaMessages.subType
                        )
                    }
                }
            }
        }

        storeMessages(messageList, mediaMessagesList)

        Log.d(TAG, " >>> List of Messages          ===> $messageList")
        Log.d(TAG, " >>> List of Media Messages    ===> $mediaMessagesList")
    }

    private fun storeMessages(messageList: MutableList<Message>, mediaMessagesList: MutableList<MediaMessage>) {
        executor.execute {
            try {
                db.messageDao().insertMultipleMessage(messageList)
                db.messageDao().insertMultipleMediaMessage(mediaMessagesList)
                dbcallback.success()
            } catch (exception: Exception) {
                Log.e(TAG, " >>> Failed To store message with error message: ${exception.localizedMessage}", exception)
                dbcallback.failure(exception.localizedMessage)
            }
        }
    }

    companion object {
        private const val TAG = "DataInitializer"
    }
}

interface DatabaseCallback {
    fun success()
    fun failure(error: String?)
}
