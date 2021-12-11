package sku.challenge.atmanatodoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item

class RemoteViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {


    private var currentPage = FetchedPage.NO_PAGE

    private val _items =
        MutableStateFlow<FetchedPageResult>(FetchedPageResult.Success(currentPage.data))

    val items: StateFlow<FetchedPageResult> = _items

    fun fetchNextPage() {
        viewModelScope.launch {
            _items.value = FetchedPageResult.Loading
            val nextPage = itemRepository.fetchRemotePage(currentPage.page + 1)
            currentPage = nextPage
            _items.value = FetchedPageResult.Success(currentPage.data)
        }
    }

    sealed class FetchedPageResult {
        class Success(val data: List<Item>) : FetchedPageResult()
        object Loading : FetchedPageResult()
    }

}