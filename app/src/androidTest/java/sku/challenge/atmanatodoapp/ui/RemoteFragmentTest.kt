package sku.challenge.atmanatodoapp.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.*
import sku.challenge.atmanatodoapp.ui.remote.RemoteFragment
import sku.challenge.atmanatodoapp.vo.FetchedPage

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class RemoteFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()


    @BindValue
    val repository: ItemRepository = mock()

    @Before
    fun setUp() = runTest {
        // Populate @Inject fields in test class
        hiltRule.inject()

        `when`(repository.fetchRemotePage(1)).thenReturn(DummyData.fetchedPage(1, 1, 20))
        `when`(repository.fetchRemotePage(2)).thenReturn(DummyData.fetchedPage(2, 20, 20))
        `when`(repository.fetchRemotePage(3)).thenReturn(FetchedPage(3, emptyList()))

        launchFragmentInHiltContainer<RemoteFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
        }
    }

    @Test
    fun itemsAreLoaded_WhenFragmentIsSetUp(): Unit = runBlocking {
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("Firstname2-1 Lastname2-1"))))

        onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())))

        onView(
            allOf(
                withId(R.id.edit_image_button),
                hasSibling(withText("person2.page1@reqres.in"))
            )
        ).check(matches(not(isDisplayed())))

        onView(
            allOf(
                withId(R.id.delete_image_button),
                hasSibling(withText("person2.page1@reqres.in"))
            )
        ).check(matches(not(isDisplayed())))

        verify(repository).fetchRemotePage(1)
    }

    // IDK how to test progress Indicator without making this test more complicated
    // skipping that for now
    @Test
    fun loadMoreData_ReachingEndOfTheList(): Unit = runBlocking {
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("Firstname2-1 Lastname2-1"))))

        onView(withId(R.id.common_list_view)).perform(ScrollAction())

        delay(100)

        onView(withId(R.id.common_list_view)).perform(ScrollAction(26))

        delay(100)

        onView(listMatcher().atPosition(22)).check(matches(hasDescendant(withText("Firstname22-2 Lastname22-2"))))

        verify(repository).fetchRemotePage(1)
        verify(repository).fetchRemotePage(2)
    }

    @Test
    @Ignore
    fun loadMoreData_WhenSwipedBeforePreviousDataIsLoaded(): Unit = runBlocking {
        // how to reproduce
        // open the app -> swipe firmly -> crash

        // found the cause it's due to synchronization
        // i think locking have fixed the problem

        // testing this case in viewModel
    }

    @Test
    fun showSnack_WhenNoDataIsFound(): Unit = runBlocking {
        onView(withId(R.id.common_list_view)).perform(ScrollAction())
        delay(100)
        onView(withId(R.id.common_list_view)).perform(ScrollAction())
        delay(200)

        onView(withText(R.string.no_more_data_available)).check(matches(isDisplayed()))

        verify(repository).fetchRemotePage(1)
        verify(repository).fetchRemotePage(2)
        verify(repository).fetchRemotePage(3)
    }


    // IDK but espresso is unable to find recyclerview with id list_view
    private fun listMatcher() = RecyclerViewMatcher(R.id.common_list_view)

    // https://stackoverflow.com/a/55990445
    class ScrollAction(private val position: Int = -1) : ViewAction {
        override fun getDescription(): String {
            return "scroll RecyclerView to bottom"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount

            val position = if (this.position != -1) {
                this.position
            } else {
                // last item in the list
                itemCount?.minus(1) ?: 0
            }

            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}