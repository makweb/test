package ru.skillbranch.skillarticles.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.skillbranch.skillarticles.data.local.PrefManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule{

    @Provides
    @Singleton
    fun providePrefManger(@ApplicationContext context:Context) : PrefManager = PrefManager(context)

}