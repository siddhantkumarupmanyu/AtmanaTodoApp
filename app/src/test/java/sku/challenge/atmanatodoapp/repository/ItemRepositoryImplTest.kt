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

}