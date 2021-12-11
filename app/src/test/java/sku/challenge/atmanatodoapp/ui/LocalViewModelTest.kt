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
import org.junit.After
import org.junit.Before
import org.junit.Test
import sku.challenge.atmanatodoapp.fake.FakeRepository
import sku.challenge.atmanatodoapp.test_utils.DummyData

@ExperimentalCoroutinesApi
class LocalViewModelTest {

    private val repository = FakeRepository()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateItemsWithoutRefreshing() = runTest {
        repository.items = listOf(DummyData.items(1, 3, 1))

        val viewModel = LocalViewModel(repository)

        yield()

        val items = viewModel.items.first()

        assertThat(items, `is`(equalTo(DummyData.items(1, 3, 1))))
    }

    // should investigate SharingStarted.Eagerly vs Lazily vs WhileSubscribed
    @Test
    fun onlyLatestItemsAreDispatched() = runTest {
        repository.items = listOf(
            DummyData.items(1, 3, 1),
            DummyData.items(7, 3, 2)
        )

        val viewModel = LocalViewModel(repository)
        yield()

        val items = viewModel.items.first()

        assertThat(items, `is`(equalTo(DummyData.items(7, 3, 2))))
    }
}