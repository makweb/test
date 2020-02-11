package ru.skillbranch.skillarticles.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.skillbranch.skillarticles.R
import java.util.*

object LocalDataHolder {
    private var isDelay = true
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val articleData = MutableLiveData<ArticleData?>(null)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val articleInfo = MutableLiveData<ArticlePersonalInfo?>(null)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val settings = MutableLiveData(AppSettings())


    fun findArticle(articleId: String): LiveData<ArticleData?> {
        GlobalScope.launch {
            if (isDelay) delay(1000)
            withContext(Dispatchers.Main){
                articleData.value = ArticleData(
                        title = "CoordinatorLayout Basic",
                        category = "Android",
                        categoryIcon = R.drawable.logo,
                        date = Date(),
                        author = "Skill-Branch"
                    )
            }

        }
        return articleData

    }

    fun findArticlePersonalInfo(articleId: String): LiveData<ArticlePersonalInfo?> {
        GlobalScope.launch {
            if (isDelay) delay(500)
            withContext(Dispatchers.Main){
                articleInfo.value = ArticlePersonalInfo(isBookmark = true)
            }
        }
        return articleInfo
    }

    fun getAppSettings() = settings
    fun updateAppSettings(appSettings: AppSettings) {
        settings.value = appSettings
    }

    fun updateArticlePersonalInfo(info: ArticlePersonalInfo) {
        articleInfo.value = info
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearData(){
        articleInfo.postValue(null)
        articleData.postValue(null)
        settings.postValue(AppSettings())
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun disableDelay(value:Boolean = false) {
        isDelay = !value
    }
}

object NetworkDataHolder {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val content = MutableLiveData<List<Any>?>(null)
    private var isDelay = true

    fun loadArticleContent(articleId: String): LiveData<List<Any>?> {
        GlobalScope.launch {
            if (isDelay) delay(1500)
            withContext(Dispatchers.Main){
                content.value = listOf(longText)
            }

        }
        return content
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun disableDelay(value:Boolean = false) {
        isDelay = !value
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearData(){
        content.postValue(null)
    }
}

data class ArticleData(
    val shareLink: String? = null,
    val title: String? = null,
    val category: String? = null,
    val categoryIcon: Any? = null,
    val date: Date,
    val author: Any? = null,
    val poster: String? = null,
    val content: List<Any> = emptyList()
)

data class ArticlePersonalInfo(
    val isLike: Boolean = false,
    val isBookmark: Boolean = false
)

data class AppSettings(
    val isDarkMode: Boolean = false,
    val isBigText: Boolean = false
)

val longText: String = """
# H1
## H2
### H3
#### H4
##### H5
###### H6

* Unordered list can use asterisks
- Or minuses
+ Or pluses

""".trimIndent()

/*
# H1
## H2
### H3
#### H4
##### H5
###### H6

Emphasis, aka italics, with *asterisks* or _underscores_.

Strong emphasis, aka bold, with **asterisks** or __underscores__.

Combined emphasis with **asterisks and _underscores_**.

Strikethrough uses two tildes. ~~Scratch this.~~

1. First ordered list item
2. Another item
⋅⋅* Unordered sub-list.
1. Actual numbers don't matter, just that it's a number
⋅⋅1. Ordered sub-list
4. And another item.

⋅⋅⋅You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).

⋅⋅⋅To have a line break without a paragraph, you will need to use two trailing spaces.⋅⋅
⋅⋅⋅Note that this line is separate, but within the same paragraph.⋅⋅
⋅⋅⋅(This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)

* Unordered list can use asterisks
- Or minuses
+ Or pluses

[I'm an inline-style link](https://www.google.com)

[I'm an inline-style link with title](https://www.google.com "Google's Homepage")

[I'm a reference-style link][Arbitrary case-insensitive reference text]

[I'm a relative reference to a repository file](../blob/master/LICENSE)

[You can use numbers for reference-style link definitions][1]

Some text to show that the reference links can follow later.

[arbitrary case-insensitive reference text]: https://www.mozilla.org
[link text itself]: http://www.reddit.com*/




/*# H1
## H2
### H3
#### H4
##### H5
###### H6

Emphasis, aka italics, with *asterisks* or _underscores_.

Strong emphasis, aka bold, with **asterisks** or __underscores__.

Combined emphasis with **asterisks and _underscores_**.

Strikethrough uses two tildes. ~~Scratch this.~~

1. First ordered list item
2. Another item
⋅⋅* Unordered sub-list.
1. Actual numbers don't matter, just that it's a number
⋅⋅1. Ordered sub-list
4. And another item.

⋅⋅⋅You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).

⋅⋅⋅To have a line break without a paragraph, you will need to use two trailing spaces.⋅⋅
⋅⋅⋅Note that this line is separate, but within the same paragraph.⋅⋅
⋅⋅⋅(This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)

* Unordered list can use asterisks
- Or minuses
+ Or pluses

[I'm an inline-style link](https://www.google.com)

[I'm an inline-style link with title](https://www.google.com "Google's Homepage")

[I'm a reference-style link][Arbitrary case-insensitive reference text]

[I'm a relative reference to a repository file](../blob/master/LICENSE)

[You can use numbers for reference-style link definitions][1]

Some text to show that the reference links can follow later.

[arbitrary case-insensitive reference text]: https://www.mozilla.org
[link text itself]: http://www.reddit.com
 */