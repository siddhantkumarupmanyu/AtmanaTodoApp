package sku.challenge.atmanatodoapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.*
import sku.challenge.atmanatodoapp.ui.local.LocalFragment
import sku.challenge.atmanatodoapp.vo.Item


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class LocalFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    @BindValue
    val repository: ItemRepository = mock()

    private val navController = mock<NavController>()

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()


        `when`(repository.getLocalItems()).thenReturn(flowOf(DummyData.items(1, 6, 1)))

        launchFragmentInHiltContainer<LocalFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun shouldNavigateToItemFragment_WhenFABIsClicked() {
        onView(withId(R.id.add_item)).perform(click())

        verify(navController).navigate(eq(ContainerFragmentDirections.actionContainerToEditItem(-1)))
    }

    @Test
    fun shouldNavigateToItemFragment_WhenItemEditIsClicked(): Unit = runBlocking {
        Thread.sleep(100)
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("person2.page1@reqres.in"))))
        onView(
            allOf(
                withId(R.id.edit_image_button),
                hasSibling(withText("person2.page1@reqres.in"))
            )
        ).perform(
            click()
        )

        verify(navController).navigate(ContainerFragmentDirections.actionContainerToEditItem(2))
    }

    @Test
    fun shouldDeleteItem() = runBlocking {
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("person2.page1@reqres.in"))))

        onView(
            allOf(
                withId(R.id.delete_image_button),
                hasSibling(withText("person2.page1@reqres.in"))
            )
        ).perform(
            click()
        )

        delay(50)

        verify(repository).deleteLocalItem(
            Item(
                "person2.page1@reqres.in",
                "Firstname2-1",
                "Lastname2-1",
                2
            )
        )
    }


    // IDK but espresso is unable to find recyclerview with id list_view
    private fun listMatcher() = RecyclerViewMatcher(R.id.common_list_view)

}