package ai.sample.room.room

import ai.sample.room.data.Message
import ai.sample.room.data.MediaMessage
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class, MediaMessage::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        private const val TAG = "AppDatabase"
        private const val DATABASE_NAME = "APP_DATABASE"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    Log.d(TAG, " >>> Creating new database instance")
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME).build()
                }
            }
            Log.d(TAG, " >>> Getting the database instance")
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}