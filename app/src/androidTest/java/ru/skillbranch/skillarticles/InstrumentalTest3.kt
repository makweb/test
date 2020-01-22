package ru.skillbranch.skillarticles

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.skillbranch.skillarticles.ui.RootActivity


@RunWith(AndroidJUnit4::class)
class InstrumentalTest3 {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun module4() {
        Thread.sleep(6000)
        onView(withId(R.id.coordinator_container))
            .perform(swipeUp(), swipeUp())
        onView(withId(R.id.bottombar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.coordinator_container))
            .perform(swipeDown(), swipeDown())
        onView(withId(R.id.bottombar)).check(matches(isDisplayed()))
    }

}