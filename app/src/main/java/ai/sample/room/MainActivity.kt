package ai.sample.room

import ai.sample.room.data.MediaMessage
import ai.sample.room.data.Message
import ai.sample.room.extention.showToast
import ai.sample.room.room.AppDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var mDB: AppDatabase
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        onClicks()

    }

    private fun init() {
        mDB = AppDatabase.getInstance(this)!!
        executor = Executors.newSingleThreadExecutor()
    }

    private fun onClicks() {
        save_btn.setOnClickListener {
            saveMessage()
        }

        get_btn.setOnClickListener {
            getMessage()
        }

        getMedia_btn.setOnClickListener {
            getMediaMessage()
        }

        delete_btn.setOnClickListener {
            deleteAllMessages()
        }
    }

    private fun saveMessage() {
        DataInitializer(this, executor, mDB, object: DatabaseCallback{
            override fun success() {
                runOnUiThread {
                    showToast("Data Store successfull")
                }
            }

            override fun failure(error: String?) {
                runOnUiThread {
                    showToast("Failed to store data with error message : \n$error")
                }
            }
        })
    }

    private fun getMessage() {
        var listmessage: List<Message?>?
        executor.execute {
            listmessage = mDB.messageDao().loadAllMessage()
            runOnUiThread {
                showToast("Loading list of messages: \n$listmessage")
            }
        }
    }

    private fun getMediaMessage() {
        var listmediamessage: List<MediaMessage?>?
        executor.execute {
            listmediamessage = mDB.messageDao().loadAllMediaMessage()
            runOnUiThread {
                showToast("Loading list of media messages: \n$listmediamessage")
            }
        }
    }

    private fun deleteAllMessages() {
        executor.execute {
            mDB.messageDao().deleteAllMessages()
            runOnUiThread {
                showToast("All Messages Deleted")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }
}
