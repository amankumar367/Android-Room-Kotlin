package ai.sample.room

import ai.sample.room.data.MediaMessage
import ai.sample.room.data.Message
import ai.sample.room.extention.showToast
import ai.sample.room.room.AppDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo.State.*
import androidx.work.WorkManager
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
        // Create a Constraints object that defines when the task should run
        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            .build()

        // ...then create a OneTimeWorkRequest that uses those constraints
        val dataInitializationWork = OneTimeWorkRequestBuilder<DataInitializerWork>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(dataInitializationWork)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(dataInitializationWork.id)
            .observe(this, Observer { workInfo ->
                // Check if the current work's state is "successfully finished"
                when (workInfo.state) {
                    SUCCEEDED -> showToast("All Data Store Successfully")
                    FAILED    -> showToast("Failed to Store Data")
                    else -> {}
                }
            })
    }

    private fun getMessage() {
        var listmessage: List<Message?>?
        executor.execute {
            listmessage = mDB.messageDao().loadAllMessage()
            runOnUiThread {
                tv_message.text = listmessage.toString()
            }
        }
    }

    private fun getMediaMessage() {
        var listmediamessage: List<MediaMessage?>?
        executor.execute {
            listmediamessage = mDB.messageDao().loadAllMediaMessage()
            runOnUiThread {
                tv_media_message.text = listmediamessage.toString()
            }
        }
    }

    private fun deleteAllMessages() {
        executor.execute {
            mDB.messageDao().deleteAllMessages()
            runOnUiThread {
                getMessage()
                getMediaMessage()
                showToast("All Messages Deleted")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }
}
