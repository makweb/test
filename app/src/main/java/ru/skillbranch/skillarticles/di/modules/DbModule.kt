package ru.skillbranch.skillarticles.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.data.local.AppDb
import ru.skillbranch.skillarticles.data.local.dao.*
import ru.skillbranch.skillarticles.data.repositories.IRepository
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context) : AppDb = Room.databaseBuilder(
        context,
        AppDb::class.java,
        AppDb.DATABASE_NAME
    ).build()


    @Provides
    @Singleton
    fun provideArticlesDao(db: AppDb): ArticlesDao = db.articlesDao()

    @Provides
    @Singleton
    fun provideArticleCountsDao(db: AppDb): ArticleCountsDao = db.articleCountsDao()

    @Provides
    @Singleton
    fun provideCategoriesDao(db: AppDb): CategoriesDao = db.categoriesDao()

    @Provides
    @Singleton
    fun provideArticlePersonalInfosDao(db: AppDb): ArticlePersonalInfosDao = db.articlePersonalInfosDao()

    @Provides
    @Singleton
    fun provideTagsDao(db: AppDb): TagsDao = db.tagsDao()

    @Provides
    @Singleton
    fun provideArticleContentsDao(db: AppDb): ArticleContentsDao = db.articleContentsDao()

}


@Module
@InstallIn(ActivityComponent::class)
abstract class RootModule  {
    @Binds
    abstract fun provideRootRepository(repository:RootRepository): IRepository
}