/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sku.challenge.atmanatodoapp.test_utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule that registers an idling resource for all fragment views that use data binding.
 */
class DataBindingIdlingResourceRule() : TestWatcher() {
    private val idlingResource = DataBindingIdlingResource()

    fun monitorFragment(fragment: Fragment) {
        idlingResource.monitorFragment(fragment)
    }

    fun <T : FragmentActivity> monitorActivity(activityScenario: ActivityScenario<T>) {
        idlingResource.monitorActivity(activityScenario)
    }

    override fun finished(description: Description?) {
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }

}