package sku.challenge.atmanatodoapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import sku.challenge.atmanatodoapp.di.AppModule
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.DataBindingIdlingResourceRule
import sku.challenge.atmanatodoapp.test_utils.launchFragmentInHiltContainer
import sku.challenge.atmanatodoapp.test_utils.mock
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

        launchFragmentInHiltContainer<LocalFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    // @Test

    @Test
    @Ignore
    fun navigateBackOnSave() {

    }

}