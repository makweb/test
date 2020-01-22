package ru.skillbranch.skillarticles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jraska.livedata.TestObserver
import com.jraska.livedata.test
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import ru.skillbranch.skillarticles.data.*
import ru.skillbranch.skillarticles.extensions.format
import ru.skillbranch.skillarticles.viewmodels.ArticleState
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.Notify
import java.lang.Thread.sleep
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class InstrumentalTest1 {
    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test(timeout = 30000)
    fun module1() {
        val vm = ArticleViewModel("0")
        val expectedDate = Date()

        vm.state.test()
            .awaitValue() //load app settings
            .assertValue(
                "load app settings",
                mapOf(
                    "isLoadingContent" to true,
                    "isDarkMode" to false,
                    "isBigText" to false
                ),
                { it.toMap() }
            )
            .awaitNextValue() //load article personal info
            .assertValue(
                "load article personal info",
                mapOf(
                    "isLoadingContent" to true,
                    "isDarkMode" to false,
                    "isBigText" to false,
                    "isLike" to false,
                    "isBookmark" to true
                ),
                { it.toMap() }
            )
            .awaitNextValue() //load article info
            .assertValue(
                "load article info",
                mapOf(
                    "isLoadingContent" to true,
                    "isDarkMode" to false,
                    "isBigText" to false,
                    "isLike" to false,
                    "isBookmark" to true,
                    "shareLink" to null,
                    "title" to "CoordinatorLayout Basic",
                    "category" to "Android",
                    "author" to "Skill-Branch",
                    "categoryIcon" to R.drawable.logo,
                    "poster" to null
                ),
                { it.toMap() }
            )
            .awaitNextValue()//load article content
            .assertValue(
                "load article content",
                mapOf(
                    "isLoadingContent" to false,
                    "isDarkMode" to false,
                    "isBigText" to false,
                    "isLike" to false,
                    "isBookmark" to true,
                    "shareLink" to null,
                    "title" to "CoordinatorLayout Basic",
                    "category" to "Android",
                    "author" to "Skill-Branch",
                    "categoryIcon" to R.drawable.logo,
                    "poster" to null,
                    "content" to listOf(longText)
                ),
                { it.toMap() }
            )
            .assertHistorySize(4)

        //change app settings
        sleep(500)
        LocalDataHolder.settings.value = AppSettings(isDarkMode = true)

        vm.state.test()
            .awaitValue()
            .assertValue(
                "change app settings",
                mapOf(
                    "isDarkMode" to true
                ),
                { it.toMap() }
            )

        //change personal info
        sleep(500)
        LocalDataHolder.articleInfo.value = ArticlePersonalInfo(isLike = true, isBookmark = false)

        vm.state.test()
            .awaitValue()
            .assertValue(
                "change personal info",
                mapOf(
                    "isLike" to true,
                    "isBookmark" to false
                ),
                { it.toMap() }
            )

        //change article data
        sleep(500)
        LocalDataHolder.articleData.value = ArticleData(
            title = "test title",
            category = "test",
            author = "test",
            shareLink = "any share link",
            date = expectedDate
        )

        vm.state.test()
            .awaitValue()
            .assertValue(
                "change article data",
                mapOf(
                    "title" to "test title",
                    "category" to "test",
                    "author" to "test",
                    "shareLink" to "any share link",
                    "date" to expectedDate.format()
                ),
                { it.toMap() }
            )

        //change content data
        sleep(500)
        NetworkDataHolder.content.value = listOf("long long text content")

        vm.state.test()
            .awaitValue()
            .assertValue(
                "change content data",
                mapOf(
                    "content" to listOf("long long text content")
                ),
                { it.toMap() }
            )
    }

    @Test(timeout = 30000)
    fun module2() {
        sleep(5000)
        LocalDataHolder.disableDelay()
        NetworkDataHolder.disableDelay()
        val vm = ArticleViewModel("0")

        //load init data
        vm.state.test()
            .awaitValue()
            .assertHasValue()

        sleep(500)
        //clear state
        vm.state.value = ArticleState()
        vm.state.test()
            .awaitValue()
            .assertValue(ArticleState())

        sleep(500)
        //like check
        vm.handleLike()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "like check",
                mapOf("isLike" to true),
                { it.toMap() }
            )

        vm.notifications
            .test()
            .awaitValue()
            .assertValue(
                "like check notification message",
                mapOf("message" to "Mark is liked"),
                { mapOf("message" to it.peekContent().message) }
            )

        sleep(500)
        //like uncheck
        vm.handleLike()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "like uncheck",
                mapOf("isLike" to false),
                { it.toMap() }
            )

        vm.notifications
            .test()
            .awaitValue()
            .assertValue(
                "like uncheck notification message",
                mapOf(
                    "msg" to "Don`t like it anymore",
                    "label" to "No, still like it"
                ),
                {
                    val (msg, label, _) = (it.peekContent() as Notify.ActionMessage)
                    mapOf("msg" to msg, "label" to label)
                }
            )


        sleep(500)
        //check Bookmark
        vm.handleBookmark()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "check Bookmark",
                mapOf("isBookmark" to true),
                { it.toMap() }
            )

        vm.notifications
            .test()
            .awaitValue()
            .assertValue(
                "check Bookmark notification message",
                mapOf("message" to "Add to bookmarks"),
                { mapOf("message" to it.peekContent().message) }
            )

        sleep(500)
        //uncheck Bookmark
        vm.handleBookmark()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "uncheck Bookmark",
                mapOf("isBookmark" to false),
                { it.toMap() }
            )

        vm.notifications
            .test()
            .awaitValue()
            .assertValue(
                "uncheck Bookmark notification message",
                mapOf("message" to "Remove from bookmarks"),
                { mapOf("message" to it.peekContent().message) }
            )
            .assertValue {
                it.peekContent().message == "Remove from bookmarks"
            }

        sleep(500)
        vm.handleUpText()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "handleUpText",
                mapOf("isBigText" to true),
                { it.toMap() }
            )

        sleep(500)
        vm.handleDownText()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "handleDownText",
                mapOf("isBigText" to false),
                { it.toMap() }
            )

        sleep(500)
        vm.handleNightMode()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "handleNightMode",
                mapOf("isDarkMode" to true),
                { it.toMap() }
            )

        sleep(500)
        vm.handleToggleMenu()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "check ToggleMenu",
                mapOf("isShowMenu" to true),
                { it.toMap() }
            )

        sleep(500)
        vm.handleToggleMenu()
        vm.state
            .test()
            .awaitValue()
            .assertValue(
                "uncheck ToggleMenu",
                mapOf("isShowMenu" to false),
                { it.toMap() }
            )

        sleep(500)
        vm.handleShare()
        vm.notifications
            .test()
            .awaitValue()
            .assertValue(
                "handleShare notification message",
                mapOf(
                    "msg" to "Share is not implemented",
                    "label" to "OK",
                    "handler" to null
                ),
                {
                    val (msg, label, handler) = (it.peekContent() as Notify.ErrorMessage)
                    mapOf("msg" to msg, "label" to label, "handler" to handler)
                }
            )

        LocalDataHolder.disableDelay(true)
        NetworkDataHolder.disableDelay(true)
    }

    private fun ArticleState.toMap(): Map<String, Any?> = mapOf(
        "isLoadingContent" to isLoadingContent,
        "isDarkMode" to isDarkMode,
        "isBigText" to isBigText,
        "isLike" to isLike,
        "isBookmark" to isBookmark,
        "shareLink" to shareLink,
        "title" to title,
        "category" to category,
        "author" to author,
        "categoryIcon" to categoryIcon,
        "poster" to poster,
        "date" to date,
        "content" to content,
        "isShowMenu" to isShowMenu
    )

    private fun <T> TestObserver<T>.assertValue(
        description: String = "",
        expectedMap: Map<String, Any?>,
        transform: ((T) -> Map<String, Any?>)
    ): TestObserver<T> {
        val actual = transform(value())
        expectedMap.forEach { (k, v) ->
            Assert.assertEquals(
                "$description property:$k",
                v,
                actual[k]
            )
        }
        return this
    }

}


