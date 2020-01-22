package ru.skillbranch.skillarticles

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.skillbranch.skillarticles.ui.RootActivity


@RunWith(AndroidJUnit4::class)
class InstrumentalTest4 {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun module5() {
        Thread.sleep(6000)
        onView(withId(R.id.btn_settings))
            .perform(click())
            .check(matches(isChecked()))

        Thread.sleep(1000)
        onView(withId(R.id.submenu))
            .check(matches(isDisplayed()))

        onView(withId(R.id.coordinator_container))
            .perform(swipeUp(), swipeUp())
        onView(withId(R.id.submenu)).check(matches(not(isDisplayed())))
    }

}