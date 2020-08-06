package ru.skillbranch.skillarticles.data.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.skillbranch.skillarticles.data.local.DbManager.db
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.local.dao.ArticleContentsDao
import ru.skillbranch.skillarticles.data.local.dao.ArticleCountsDao
import ru.skillbranch.skillarticles.data.local.dao.ArticlePersonalInfosDao
import ru.skillbranch.skillarticles.data.local.dao.ArticlesDao
import ru.skillbranch.skillarticles.data.local.entities.ArticleFull
import ru.skillbranch.skillarticles.data.models.AppSettings
import ru.skillbranch.skillarticles.data.remote.NetworkManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.req.MessageReq
import ru.skillbranch.skillarticles.data.remote.res.CommentRes
import ru.skillbranch.skillarticles.extensions.data.toArticleContent

interface IArticleRepository {
    fun findArticle(articleId: String): LiveData<ArticleFull>
    fun getAppSettings(): LiveData<AppSettings>
    suspend fun toggleLike(articleId: String)
    suspend fun toggleBookmark(articleId: String)
    fun isAuth(): LiveData<Boolean>
    suspend fun sendMessage(articleId: String, text: String, answerToSlug: String?)
    fun loadAllComments(articleId: String, totalCount: Int, errHandler: (Throwable)-> Unit): CommentsDataFactory
    suspend fun decrementLike(articleId: String)
    suspend fun incrementLike(articleId: String)
    fun updateSettings(copy: AppSettings)
    suspend fun fetchArticleContent(articleId: String)
    fun findArticleCommentCount(articleId: String): LiveData<Int>
}

object ArticleRepository : IArticleRepository {
    private val network = NetworkManager.api
    private val preferences = PrefManager
    private var articlesDao = db.articlesDao()
    private var articlePersonalDao = db.articlePersonalInfosDao()
    private var articleCountsDao = db.articleCountsDao()
    private var articleContentDao = db.articleContentsDao()

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setupTestDao(
        articlesDao:ArticlesDao,
        articlePersonalDao:ArticlePersonalInfosDao,
        articleCountsDao:ArticleCountsDao,
        articleContentDao:ArticleContentsDao
    ) {
        this.articlesDao = articlesDao
        this.articlePersonalDao = articlePersonalDao
        this.articleCountsDao = articleCountsDao
        this.articleContentDao = articleContentDao
    }

    override fun findArticle(articleId: String): LiveData<ArticleFull> {
        return articlesDao.findFullArticle(articleId)
    }

    override fun getAppSettings(): LiveData<AppSettings> = preferences.appSettings //from preferences

    override suspend fun toggleLike(articleId: String) {
        articlePersonalDao.toggleLikeOrInsert(articleId)
    }

    override suspend fun toggleBookmark(articleId: String) {
        articlePersonalDao.toggleBookmarkOrInsert(articleId)
    }

    override fun updateSettings(settings: AppSettings) {
        preferences.isBigText = settings.isBigText
        preferences.isDarkMode = settings.isDarkMode
    }

    override suspend fun fetchArticleContent(articleId: String) {
        val content = network.loadArticleContent(articleId)
        articleContentDao.insert(content.toArticleContent())
    }

    override fun findArticleCommentCount(articleId: String): LiveData<Int> {
        return articleCountsDao.getCommentsCount(articleId)
    }

    override fun isAuth(): LiveData<Boolean> = preferences.isAuthLive

    override fun loadAllComments(articleId: String, totalCount: Int, errHandler: (Throwable)-> Unit) =
        CommentsDataFactory(
            itemProvider = network,
            articleId = articleId,
            totalCount = totalCount,
            errHandler = errHandler
        )

    override suspend fun decrementLike(articleId: String) {

        //check auth locally
        if(preferences.accessToken.isEmpty()) {
            articleCountsDao.decrementLike(articleId)
            return
        }

        try {
            val res = network.decrementLike(articleId, preferences.accessToken)
            articleCountsDao.updateLike(articleId, res.likeCount)
        }catch (e: Throwable){
            articleCountsDao.decrementLike(articleId)
            throw e
        }
    }

    override suspend fun incrementLike(articleId: String) {

        if(preferences.accessToken.isEmpty()) {
            articleCountsDao.incrementLike(articleId)
            return
        }

        try {
            val res = network.incrementLike(articleId, preferences.accessToken)
            articleCountsDao.updateLike(articleId, res.likeCount)
        }catch (e: Throwable){
            articleCountsDao.incrementLike(articleId)
            throw e
        }
    }

    override suspend fun sendMessage(articleId: String, message: String, answerToMessageId: String?) {
        val (_, messageCount) = network.sendMessage(
            articleId,
            MessageReq(message, answerToMessageId),
            preferences.accessToken
        )
        Log.e("ArticleRepository", "update : $messageCount");
        articleCountsDao.updateCommentsCount(articleId, messageCount)
    }

    suspend fun refreshCommentsCount(articleId: String) {
        val counts = network.loadArticleCounts(articleId)
        articleCountsDao.updateCommentsCount(articleId, counts.comments)
    }
}

class CommentsDataFactory(
    private val itemProvider: RestService,
    private val articleId: String,
    private val totalCount: Int,
    private val errHandler: (Throwable) -> Unit
) : DataSource.Factory<String?, CommentRes>() {
    override fun create(): DataSource<String?, CommentRes> =
        CommentsDataSource(itemProvider, articleId, totalCount, errHandler)

}

class CommentsDataSource(
    private val itemProvider: RestService,
    private val articleId: String,
    private val totalCount: Int,
    private val errHandler: (Throwable) -> Unit
) : ItemKeyedDataSource<String, CommentRes>() {

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<CommentRes>
    ) {

        itemProvider.loadComments(articleId, null, params.requestedLoadSize)
            .enqueue(object: Callback<List<CommentRes>>{
                override fun onFailure(call: Call<List<CommentRes>>, t: Throwable) {
                    Log.e("ArticleRepository", "tr: $t");
                    errHandler.invoke(t)
                }

                override fun onResponse(
                    call: Call<List<CommentRes>>,
                    response: Response<List<CommentRes>>
                ) {
                    Log.e(
                        "ArticleRepository",
                        "loadInitial: tc: $totalCount ${response.body()?.size} init key: ${params.requestedInitialKey} size: ${response.body()!!.size}"
                    );
                    callback.onResult(
                        if (totalCount > 0) response.body()!!.take(totalCount) else emptyList(),
                        0,
                        totalCount
                    )
                }

            })

    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<CommentRes>) {
        itemProvider.loadComments(articleId, params.key, params.requestedLoadSize)
            .enqueue(object: Callback<List<CommentRes>>{
                override fun onFailure(call: Call<List<CommentRes>>, t: Throwable) {
                    errHandler.invoke(t)
                }

                override fun onResponse(
                    call: Call<List<CommentRes>>,
                    response: Response<List<CommentRes>>
                ) {
                    callback.onResult(response.body()!!)
                }

            })

    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<CommentRes>) {
        itemProvider.loadComments(articleId, params.key, -params.requestedLoadSize)
            .enqueue(object: Callback<List<CommentRes>>{
                override fun onFailure(call: Call<List<CommentRes>>, t: Throwable) {
                    errHandler.invoke(t)
                }

                override fun onResponse(
                    call: Call<List<CommentRes>>,
                    response: Response<List<CommentRes>>
                ) {
                    callback.onResult(response.body()!!)
                }

            })
    }

    //use ID as key not slug -> slug only draw threads
    override fun getKey(item: CommentRes): String = item.id

}
