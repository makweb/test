package ru.skillbranch.skillarticles.data.repositories

import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.models.User
import ru.skillbranch.skillarticles.data.remote.NetworkManager
import ru.skillbranch.skillarticles.data.remote.req.EditProfileReq
interface IProfileRepository {
    fun getProfile(): LiveData<User?>
    suspend fun uploadAvatar(body: MultipartBody.Part)
    suspend fun removeAvatar()
    suspend fun editProfile(name: String, about: String)
}

object ProfileRepository : IProfileRepository {
    private val prefs = PrefManager(App.applicationContext(), Moshi.Builder().build())
    private val network = NetworkManager.api

    override fun getProfile(): LiveData<User?> = prefs.profileLive

    override suspend fun uploadAvatar(body: MultipartBody.Part) {
        val (url) = network.upload(body, prefs.accessToken)
        prefs.profile = prefs.profile?.copy(avatar = url)
    }

    override suspend fun removeAvatar() {
        network.removeProfileAvatar(prefs.accessToken)
        prefs.profile = prefs.profile!!.copy(avatar = "")
    }

    override suspend fun editProfile(name: String, about: String) {
        val user = network.editProfile(EditProfileReq(name, about), prefs.accessToken)
        prefs.profile = user
    }
}