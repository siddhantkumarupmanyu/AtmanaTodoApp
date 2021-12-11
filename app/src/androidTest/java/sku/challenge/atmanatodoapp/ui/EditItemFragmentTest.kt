package sku.challenge.atmanatodoapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.DataBindingIdlingResourceRule
import sku.challenge.atmanatodoapp.test_utils.launchFragmentInHiltContainer
import sku.challenge.atmanatodoapp.test_utils.mock
import sku.challenge.atmanatodoapp.ui.edit_item.EditItemFragment
import sku.challenge.atmanatodoapp.vo.Item


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class EditItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    @BindValue
    val repository: ItemRepository = mock()

    private val navController = mock<NavController>()

    private val item = Item("email", "fname", "lname", 1)

    @Before
    fun setUp() = runTest {
        // Populate @Inject fields in test class
        hiltRule.inject()

        `when`(repository.getItem(1)).thenReturn(item)
    }

    @Test
    fun addNewItem(): Unit = runBlocking {
        val args = EditItemFragmentArgs(-1).toBundle()
        launchFragmentInHiltContainer<EditItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.first_name_edit_text)).check(matches(withText("")))
        onView(withId(R.id.last_name_edit_text)).check(matches(withText("")))
        onView(withId(R.id.email_edit_text)).check(matches(withText("")))

        onView(withId(R.id.first_name_edit_text)).perform(typeText("firstName"))
        onView(withId(R.id.last_name_edit_text)).perform(typeText("lastName"))
        onView(withId(R.id.email_edit_text)).perform(typeText("email"))

        onView(withId(R.id.save)).perform(click())

        verify(navController).navigateUp()
        verify(repository).saveLocalItem(Item("email", "firstName", "lastName", 0))
    }

    @Test
    fun editItem() = runBlocking {
        val args = EditItemFragmentArgs(1).toBundle()
        launchFragmentInHiltContainer<EditItemFragment>(fragmentArgs = args) {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.first_name_edit_text)).check(matches(withText(item.firstName)))
        onView(withId(R.id.last_name_edit_text)).check(matches(withText(item.lastName)))
        onView(withId(R.id.email_edit_text)).check(matches(withText(item.email)))

        onView(withId(R.id.first_name_edit_text)).perform(clearText(), typeText("editFirstName"))
        onView(withId(R.id.last_name_edit_text)).perform(clearText(), typeText("editLastName"))
        onView(withId(R.id.email_edit_text)).perform(clearText(), typeText("editEmail"))

        onView(withId(R.id.save)).perform(click())

        verify(navController).navigateUp()
        verify(repository).saveLocalItem(Item("editEmail", "editFirstName", "editLastName", 1))
    }

}