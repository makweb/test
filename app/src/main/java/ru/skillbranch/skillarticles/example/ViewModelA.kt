package ru.skillbranch.skillarticles.example

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import ru.skillbranch.skillarticles.data.repositories.RootRepository

class ViewModelA @AssistedInject constructor(
    @Assisted val handler: SavedStateHandle,
    val repository: RootRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory: ViewModelAssistedFactory<ViewModelA>

}