package ru.skillbranch.skillarticles.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.di.modules.*
import javax.inject.Singleton

@Singleton
@Component(modules = [PreferencesModule::class, NetworkUtilsModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(app: App)

    val activityComponent : ActivityComponent.Factory
}