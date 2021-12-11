package sku.challenge.atmanatodoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sku.challenge.atmanatodoapp.di.BaseUrl
import sku.challenge.atmanatodoapp.di.ConstantsModule
import sku.challenge.atmanatodoapp.test_utils.OkHttp3IdlingResource
import sku.challenge.atmanatodoapp.test_utils.RecyclerViewMatcher
import sku.challenge.atmanatodoapp.test_utils.enqueueResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@LargeTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(ConstantsModule::class)
@HiltAndroidTest
class EndToEndTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @BindValue
    val baseUrl: BaseUrl = BaseUrl("http://127.0.0.1:8080")

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun startServer() {
        hiltRule.inject()

        setupRetrofitClient()
    }

    @Test
    fun localItems() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withText("LOCAL")).perform(click())

        onView(withId(R.id.add_item)).perform(click())
        onView(withId(R.id.first_name_edit_text)).perform(typeText("firstName"))
        onView(withId(R.id.last_name_edit_text)).perform(typeText("lastName"))
        onView(withId(R.id.email_edit_text)).perform(typeText("email1@example.com"))
        onView(withId(R.id.save)).perform(click())

        onView(withId(R.id.add_item)).perform(click())
        onView(withId(R.id.first_name_edit_text)).perform(typeText("firstName"))
        onView(withId(R.id.last_name_edit_text)).perform(typeText("lastName"))
        onView(withId(R.id.email_edit_text)).perform(typeText("email2@example.com"))
        onView(withId(R.id.save)).perform(click())

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("email1@example.com"))))
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("email2@example.com"))))

        onView(
            allOf(
                withId(R.id.edit_image_button),
                hasSibling(withText("email1@example.com"))
            )
        ).perform(
            click()
        )

        onView(withId(R.id.email_edit_text)).perform(
            clearText(),
            typeText("email-edited1@example.com")
        )
        onView(withId(R.id.save)).perform(click())

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("email-edited1@example.com"))))

        onView(
            allOf(
                withId(R.id.delete_image_button),
                hasSibling(withText("email-edited1@example.com"))
            )
        ).perform(
            click()
        )

        onView(listMatcher().atPosition(0)).check(matches(not(hasDescendant(withText("email-edited1@example.com")))))
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("email2@example.com"))))

        activityScenario.close()
    }

    @Test
    @Ignore
    fun remoteItems() {
        mockWebServer.start(8080)
        enqueueResponses()

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // load the first page
        assertRequest("/users?page=1")

        Thread.sleep(100)

        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("Janet Weaver"))))

        // test pagination

        // no data available

        activityScenario.close()
    }

    private fun assertRequest(path: String) {
        val request = mockWebServer.takeRequest(2, TimeUnit.SECONDS)
        MatcherAssert.assertThat(request.path, CoreMatchers.`is`(path))
    }

    private fun enqueueResponses() {
        mockWebServer.enqueueResponse("page1.json")
        mockWebServer.enqueueResponse("page2.json")
        mockWebServer.enqueueResponse("page3.json")
    }

    // IDK but espresso is unable to find recyclerview with id list_view
    private fun listMatcher() = RecyclerViewMatcher(R.id.common_list_view)

    private fun setupRetrofitClient() {
        val resource = OkHttp3IdlingResource.create("okHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }
}