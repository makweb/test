package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import java.io.Serializable

interface VMState : Serializable {
    fun toBundle(): Bundle? = null
    fun fromBundle(bundle: Bundle): VMState? = null
}