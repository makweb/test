package ru.skillbranch.skillarticles.di.modules

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.skillbranch.skillarticles.data.local.entities.ArticleItem
import ru.skillbranch.skillarticles.ui.RootActivity
import ru.skillbranch.skillarticles.ui.articles.ArticlesFragment

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {
    @Provides
    fun provideActivity(activity: Activity): RootActivity = activity as RootActivity

}

