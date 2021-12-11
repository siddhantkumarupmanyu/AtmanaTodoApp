package sku.challenge.atmanatodoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
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

    // back navigation is working correctly -> I should consider another UI related test,
    //  I do not think it has anything functionality apart from Ui
    @Test
    fun localItems() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withText("LOCAL")).perform(click())

        onView(withId(R.id.add_item)).perform(click())


        // TODO:

        activityScenario.close()
        // insert an item
        // edit that item
        // delete that item
    }

    @Test
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

    private fun listMatcher() = RecyclerViewMatcher(R.id.list_view)

    private fun setupRetrofitClient() {
        val resource = OkHttp3IdlingResource.create("okHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }
}