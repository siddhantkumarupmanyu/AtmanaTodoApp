package sku.challenge.atmanatodoapp.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.db.ItemsDao
import sku.challenge.atmanatodoapp.test_utils.mock

@ExperimentalCoroutinesApi
class ItemRepositoryImplTest {

    private val apiService = mock<ApiService>()

    private val dao = mock<ItemsDao>()

    private val repository = ItemRepositoryImpl(apiService, dao)

    @Test
    fun fetchRemotePage() = runTest {
        repository.fetchRemotePage(1)
        repository.fetchRemotePage(2)

        verify(apiService).getPage(1)
        verify(apiService).getPage(2)
    }

    @Test
    fun localItems() = runTest {
        repository.getLocalItems()

        repository.getItem(1)

        verify(dao).getItems()
        verify(dao).getItem(1)
    }

}