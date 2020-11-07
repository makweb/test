package ru.skillbranch.skillarticles

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkMonitor
import ru.skillbranch.skillarticles.di.components.ActivityComponent
import ru.skillbranch.skillarticles.di.components.AppComponent
import ru.skillbranch.skillarticles.di.components.DaggerAppComponent
import javax.inject.Inject

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var activityComponent: ActivityComponent
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }



    @Inject
    lateinit var monitor : NetworkMonitor
    @Inject
    lateinit var preferences : PrefManager

    init {
        instance = this
        Log.e("App", "variant: ${BuildConfig.BUILD_TYPE}");
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(applicationContext)

        appComponent.inject(this)

        //start network monitoring
        monitor.registerNetworkMonitor()

        //set saved night/day mode
        val mode = if (preferences.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}