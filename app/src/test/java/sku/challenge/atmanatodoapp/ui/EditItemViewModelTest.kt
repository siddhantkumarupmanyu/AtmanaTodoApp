package sku.challenge.atmanatodoapp.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.test_utils.mock
import sku.challenge.atmanatodoapp.ui.edit_item.EditItemViewModel
import sku.challenge.atmanatodoapp.vo.Item


@ExperimentalCoroutinesApi
class EditItemViewModelTest {


    private val repository = mock<ItemRepository>()
    private val viewModel = EditItemViewModel(repository)

    private val item = Item("email", "fname", "lname", 1)

    @Before
    fun setUp() = runTest {
        `when`(repository.getItem(1)).thenReturn(item)

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadItem() = runTest {
        viewModel.loadItem(1)
        yield()

        val event = viewModel.event.first()
        assertThat(
            event,
            IsInstanceOf(EditItemViewModel.Event.ItemEvent::class.java)
        )
        event as EditItemViewModel.Event.ItemEvent
        assertThat(
            event.item,
            `is`(equalTo(item))
        )

        verify(repository).getItem(1)
    }

    @Test
    fun saveNewItem() = runTest {
        viewModel.saveItem("newEmail", "newFirstName", "newLastName")
        yield()

        assertThat(
            viewModel.event.first(),
            IsInstanceOf(EditItemViewModel.Event.SaveEvent::class.java)
        )

        verify(repository).saveLocalItem(
            Item(
                email = "newEmail",
                firstName = "newFirstName",
                lastName = "newLastName",
                id = 0
            )
        )
    }


    @Test
    fun editItem() = runTest {
        viewModel.loadItem(1)
        yield()

        viewModel.saveItem("editedEmail", "editedFirstName", "editedLastName")
        yield()

        assertThat(
            viewModel.event.first(),
            IsInstanceOf(EditItemViewModel.Event.SaveEvent::class.java)
        )

        verify(repository).saveLocalItem(
            item.copy(
                email = "editedEmail",
                firstName = "editedFirstName",
                lastName = "editedLastName"
            )
        )
    }
}