package ru.skillbranch.skillarticles.di.components

import android.content.Context
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.di.modules.*
import ru.skillbranch.skillarticles.di.scopes.ActivityScope
import ru.skillbranch.skillarticles.example.TestActivity

@ActivityScope
@Subcomponent( modules = [ActivityModule::class, AssistedVMModule::class])
interface ActivityComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: TestActivity): ActivityComponent
    }

    fun inject(activity: TestActivity)

    fun plusFragmentBComponent() : FragmentBComponent
    fun plusFragmentAComponent() : FragmentAComponent
}