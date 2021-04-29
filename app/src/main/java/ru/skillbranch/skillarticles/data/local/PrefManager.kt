package ru.skillbranch.skillarticles.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.delegates.PrefDelegate
import ru.skillbranch.skillarticles.data.delegates.PrefObjDelegate
import ru.skillbranch.skillarticles.data.models.AppSettings
import ru.skillbranch.skillarticles.data.models.User
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import ru.skillbranch.skillarticles.data.adapters.UserJsonAdapter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefManager(context: Context) {
    val dataStore = context.dataStore
    private val errHandler = CoroutineExceptionHandler { _, th ->
        Log.e("PrefManager", "err ${th.message}")
        //TODO handle error this
    }

    internal val scope = CoroutineScope(SupervisorJob() + errHandler)

    var isDarkMode by PrefDelegate(false)
    var isBigText by PrefDelegate(false)
    var accessToken by PrefDelegate("")
    var refreshToken by PrefDelegate("")
    var profile: User? by PrefObjDelegate(UserJsonAdapter())

    val isAuthLive: LiveData<Boolean>  = dataStore.data.map { it[stringPreferencesKey(this::accessToken.name)] ?: "" }
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
        .asLiveData()


     val profileLive: LiveData<User?>  = dataStore.data.map { it[stringPreferencesKey(this::profile.name)] ?: "" }
         .map { UserJsonAdapter().fromJson(it) }
         .distinctUntilChanged()
         .asLiveData()

    val settings: LiveData<AppSettings>
        get() {
            val isBig =
                dataStore.data.map { it[booleanPreferencesKey(this::isBigText.name)] ?: false }
            val isDark =
                dataStore.data.map { it[booleanPreferencesKey(this::isDarkMode.name)] ?: false }

            return isDark.zip(isBig) { dark, big -> AppSettings(dark, big) }
                .distinctUntilChanged()
                .asLiveData()
        }

}