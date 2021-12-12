package sku.challenge.atmanatodoapp.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.fake.FakeRepository
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.DataBindingIdlingResourceRule
import sku.challenge.atmanatodoapp.test_utils.DummyData
import sku.challenge.atmanatodoapp.test_utils.RecyclerViewMatcher
import sku.challenge.atmanatodoapp.test_utils.launchFragmentInHiltContainer
import sku.challenge.atmanatodoapp.ui.remote.RemoteFragment

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
    val repository: ItemRepository = FakeRepository()

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()

        repository as FakeRepository
        repository.pageNo = 1
        repository.delayBeforeReturningResult = 10L
        repository.fetchedPage = DummyData.fetchedPage(1, 1, 20)

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
    }

    @Test
    @Ignore
    fun loadMoreData_WhenClickedOnLoadMore() {
        repository as FakeRepository

        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("Firstname2-1 Lastname2-1"))))

        repository.pageNo = 2
        repository.delayBeforeReturningResult = 10L
        repository.fetchedPage = DummyData.fetchedPage(2, 7, 6)


        onView(listMatcher().atPosition(6)).check(matches(hasDescendant(withText("Load More"))))

    }

    @Test
    @Ignore
    fun showSnack_WhenNoDataIsFound() {

    }


    // IDK but espresso is unable to find recyclerview with id list_view
    private fun listMatcher() = RecyclerViewMatcher(R.id.common_list_view)
}