package sku.challenge.atmanatodoapp.ui

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

    private val _items =
        MutableStateFlow<FetchedPageResult>(FetchedPageResult.Success(currentPage.data))

    val items: StateFlow<FetchedPageResult> = _items

    fun fetchNextPage() {
        viewModelScope.launch {
            if (isCurrentPageWithEmptyData()) {
                return@launch
            }
            var items = (items.value as FetchedPageResult.Success).data
            _items.value = FetchedPageResult.Loading
            val nextPage = itemRepository.fetchRemotePage(currentPage.page + 1)
            currentPage = nextPage
            items = items + nextPage.data
            _items.value = FetchedPageResult.Success(items)
        }
    }

    private fun isCurrentPageWithEmptyData() =
        (currentPage != FetchedPage.NO_PAGE) && currentPage.data.isEmpty()

    sealed class FetchedPageResult {
        class Success(val data: List<Item>) : FetchedPageResult()
        object Loading : FetchedPageResult()
    }

}