package ru.skillbranch.skillarticles.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.skillbranch.skillarticles.data.local.AppDb
import ru.skillbranch.skillarticles.data.local.dao.*
import javax.inject.Singleton


@Module
object DbModule {

    @Provides
    @Singleton
//    @RootScope
    fun provideDb(appContext :Context): AppDb = Room.databaseBuilder(
        appContext,
        AppDb::class.java, AppDb.DATABASE_NAME
    )
        .build()

    @Provides
    @Singleton
//    @RootScope
    fun provideArticlesDao(db :AppDb): ArticlesDao = db.articlesDao()

    @Provides
    @Singleton
//    @RootScope
    fun provideArticlesPersonalInfoDao(db :AppDb): ArticlePersonalInfosDao = db.articlePersonalInfosDao()

    @Provides
    @Singleton
//    @RootScope
    fun provideCategoriesDao(db :AppDb): CategoriesDao = db.categoriesDao()

    @Provides
    @Singleton
//    @RootScope
    fun provideTagsDao(db :AppDb): TagsDao = db.tagsDao()

    @Provides
    @Singleton
//    @RootScope
    fun provideArticleCountsDao(db :AppDb): ArticleCountsDao = db.articleCountsDao()

}
