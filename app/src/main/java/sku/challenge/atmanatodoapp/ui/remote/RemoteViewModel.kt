package sku.challenge.atmanatodoapp.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private var currentPage = FetchedPage.NO_PAGE

    private val _items =
        MutableStateFlow<FetchedPageResult>(FetchedPageResult.Success(currentPage.data))

    val items: StateFlow<FetchedPageResult> = _items

    private val mutex = Mutex()

    fun fetchNextPage() {
        viewModelScope.launch {
            mutex.lock()

            if (!isMoreDataAvailable()) {
                return@launch
            }

            val oldItems = (items.value as FetchedPageResult.Success).data

            _items.value = FetchedPageResult.Loading

            val nextPage = itemRepository.fetchRemotePage(currentPage.page + 1)
            currentPage = nextPage

            if (nextPage.data.isEmpty()) {
                _items.value = FetchedPageResult.NoMoreDataAvailable(oldItems)
            } else {
                val combinedItems = oldItems + nextPage.data
                _items.value = FetchedPageResult.Success(combinedItems)
            }

            mutex.unlock()
        }
    }

    private fun isMoreDataAvailable() =
        items.value !is FetchedPageResult.NoMoreDataAvailable

    sealed class FetchedPageResult {
        class Success(val data: List<Item>) : FetchedPageResult()
        object Loading : FetchedPageResult()
        class NoMoreDataAvailable(val allData: List<Item>) : FetchedPageResult()
    }

}