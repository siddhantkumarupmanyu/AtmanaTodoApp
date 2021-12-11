package sku.challenge.atmanatodoapp.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {


    private var currentPage = FetchedPage.NO_PAGE

    private var combinedItems = emptyList<Item>()

    private val _items =
        MutableStateFlow<FetchedPageResult>(FetchedPageResult.Success(currentPage.data))

    val items: StateFlow<FetchedPageResult> = _items

    fun fetchNextPage() {
        viewModelScope.launch {
            if (isCurrentPageWithEmptyData()) {
                return@launch
            }
            _items.value = FetchedPageResult.Loading
            val nextPage = itemRepository.fetchRemotePage(currentPage.page + 1)
            currentPage = nextPage
            
            if (nextPage.data.isEmpty()) {
                _items.value = FetchedPageResult.NoMoreDataAvailable(combinedItems)
            } else {
                combinedItems = combinedItems + nextPage.data
                _items.value = FetchedPageResult.Success(combinedItems)
            }
        }
    }

    private fun isCurrentPageWithEmptyData() =
        (currentPage != FetchedPage.NO_PAGE) && currentPage.data.isEmpty()

    sealed class FetchedPageResult {
        class Success(val data: List<Item>) : FetchedPageResult()
        object Loading : FetchedPageResult()
        class NoMoreDataAvailable(val allData: List<Item>) : FetchedPageResult()
    }

}