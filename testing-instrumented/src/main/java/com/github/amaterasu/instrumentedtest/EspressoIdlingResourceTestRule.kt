package com.github.amaterasu.instrumentedtest

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class EspressoIdlingResourceTestRule : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    override fun finished(description: Description) {
        super.finished(description)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

}