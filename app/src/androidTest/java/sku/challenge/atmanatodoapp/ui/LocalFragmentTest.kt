package sku.challenge.atmanatodoapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.fake.FakeRepository
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.DataBindingIdlingResourceRule
import sku.challenge.atmanatodoapp.test_utils.launchFragmentInHiltContainer
import sku.challenge.atmanatodoapp.test_utils.mock


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
    val repository: ItemRepository = FakeRepository()

    private val navController = mock<NavController>()

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()

        launchFragmentInHiltContainer<LocalFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }
    }


    @Test
    fun shouldNavigateToItemFragment_WhenFABIsClicked() {
        onView(withId(R.id.add_item)).perform(click())

        verify(navController).navigate(eq(LocalFragmentDirections.actionEditItem(-1)))
    }


}