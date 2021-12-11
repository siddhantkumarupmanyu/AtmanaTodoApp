package sku.challenge.atmanatodoapp.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.db.ItemsDao
import sku.challenge.atmanatodoapp.test_utils.mock
import sku.challenge.atmanatodoapp.vo.Item

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

    @Test
    fun editAndDeleteLocalItem() = runTest {
        val item1 = Item("1@eg.com", "f1", "l1")

        repository.saveLocalItem(item1)
        repository.saveLocalItem(item1.copy(id = 1))

        repository.deleteLocalItem(item1)

        verify(dao, times(1)).insertItems(item1)
        verify(dao, times(1)).insertItems(item1.copy(id = 1))
        verify(dao, times(1)).insertItems(item1.copy(id = 1))
    }

}