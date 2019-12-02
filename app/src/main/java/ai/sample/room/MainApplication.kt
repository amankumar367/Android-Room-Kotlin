package ai.sample.room

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Log.d(TAG, " >>> Initializing Stetho")
        }
    }

    companion object {
        private const val TAG = "MainApplication"
    }

}