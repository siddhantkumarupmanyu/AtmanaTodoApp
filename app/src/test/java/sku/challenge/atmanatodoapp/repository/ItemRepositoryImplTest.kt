package sku.challenge.atmanatodoapp.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.test_utils.mock
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item

@ExperimentalCoroutinesApi
class ItemRepositoryImplTest {

    private val apiService = mock<ApiService>()

    private val repository = ItemRepositoryImpl(apiService)

    @Test
    fun fetchRemotePage() = runTest {
        repository.fetchRemotePage(1)
        repository.fetchRemotePage(2)

        verify(apiService).getPage(1)
        verify(apiService).getPage(2)
    }

    private val items = listOf(
        Item(1, "george.bluth@reqres.in", "George", "Bluth"),
        Item(2, "janet.weaver@reqres.in", "Janet", "Weaver"),
        Item(3, "emma.wong@reqres.in", "Emma", "Wong"),
    )

    private val fetchedPage = FetchedPage(1, items)

}