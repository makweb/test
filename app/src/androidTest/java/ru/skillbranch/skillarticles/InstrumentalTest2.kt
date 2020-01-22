package ru.skillbranch.skillarticles

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.skillbranch.skillarticles.ui.RootActivity
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class InstrumentalTest2 {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun module3() {
        sleep(5000)

        onView(withId(R.id.action_search)).perform(click());
        sleep(1000)

        onView(withId(R.id.search_src_text)).perform(replaceText("test"));
        //onView(isRoot()).perform(ViewActions.pressBack());
        //sleep(1000)
        //onView(withId(R.id.btn_settings))
        //    .perform(click())
        //sleep(1000)
        //onView(withId(R.id.switch_mode))
        //    .perform(click())
        //sleep(1000)
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape(mActivityTestRule.activity))
        onView(withId(R.id.search_src_text)).check(matches(withText("test")))
    }

    class OrientationChangeAction(private val orientation: Int, val activity: Activity) : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "change orientation to $orientation"
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            activity.requestedOrientation = orientation

            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                Stage.RESUMED)
            if (resumedActivities.isEmpty()) {
                throw RuntimeException("Could not change orientation")
            }
        }

        companion object {

            fun orientationLandscape(activity: Activity): ViewAction {
                return OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, activity)
            }

            fun orientationPortrait(activity: Activity): ViewAction {
                return OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, activity)
            }
        }
    }
}