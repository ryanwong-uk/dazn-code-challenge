/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.schedule

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.launchFragmentInHiltContainer
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SchedulesFragmentTest {
    @Inject
    lateinit var repository: FakeRepository

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun repositoryEmpty_showNoData() = runBlockingTest {
        // GIVEN - Repository has no events to supply
        repository.submitScheduleList(emptyList())
        repository.refreshSchedule()

        // WHEN - Launching the fragment
        // Note: Originally we use launchFragmentInContainer
        // But due to library bugs, we use launchFragmentInHiltContainer
        // See HiltExt.kt for details
        launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun repositoryNonEmpty_HiddenNoData() = runBlockingTest {
        // GIVEN - Repository has 1 schedule to supply
        repository.submitScheduleList(listOf(schedule1))
        repository.refreshSchedule()

        // WHEN - Launching the fragment
        launchFragmentInContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is NOT shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(matches(Matchers.not(isDisplayed())))
    }

    @Test
    fun repositoryError_ShowErrorDialog() = runBlockingTest {
        // GIVEN - the repository is set to always return error
        val errorMessage = "Instrumentation test error"
        repository.setReturnError(true, errorMessage)

        // WHEN - Launching the fragment
        launchFragmentInContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - error dialog is shown
        // Expects reminder to be saved successfully
        Espresso.onView(ViewMatchers.withText(R.string.something_went_wrong))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun wait31Seconds_ScheudleRefreshed() = runBlockingTest {
        repository.submitScheduleList(listOf(schedule1))
        repository.refreshSchedule()

        // GIVEN - On the Schedule List screen we have schedule1
        launchFragmentInContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
        Espresso.onView(withText(schedule1.title)).check(matches(isDisplayed()))

        // WHEN - Repository returns a new schedule list after 31 seconds
        repository.submitScheduleList(listOf(schedule2))
        repository.refreshSchedule()
        Thread.sleep(31000)

        // THEN - RecyclerView should show schedule2
        Espresso.onView(withText(schedule2.title)).check(matches(isDisplayed()))
    }
}