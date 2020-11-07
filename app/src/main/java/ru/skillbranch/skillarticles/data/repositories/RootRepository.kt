package ru.skillbranch.skillarticles.data.repositories

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.req.LoginReq
import javax.inject.Inject

class RootRepository @Inject constructor(
    private val preferences: PrefManager,
    private val network: RestService
) : IRepository{

    fun isAuth(): LiveData<Boolean> = preferences.isAuthLive

    suspend fun login(login: String, password: String) {
        val auth = network.login(LoginReq(login, password))
        preferences.profile = auth.user
        preferences.accessToken = "Bearer ${auth.accessToken}"
        preferences.refreshToken = auth.refreshToken
    }
}