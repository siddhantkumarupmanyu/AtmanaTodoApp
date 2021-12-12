package sku.challenge.atmanatodoapp.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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


    private val queue = ArrayDeque<Job>(2)

    fun fetchNextPage() {
        val job = viewModelScope.launch {
            if (!isMoreDataAvailable()) {
                return@launch
            }

            // I should handle the case when this is called multiple times right

            // println("before job check")
            // println("launched")

            // unfortunately this code is untested
            // val firstJob = queue.firstOrNull()
            // if ((firstJob != null) && firstJob.isActive) {
            //     firstJob.join()
            //     queue.removeFirst()
            // }

            // println("after job check")

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
        }

        queue.add(job)
    }

    private fun isMoreDataAvailable() =
        items.value !is FetchedPageResult.NoMoreDataAvailable

    sealed class FetchedPageResult {
        class Success(val data: List<Item>) : FetchedPageResult()
        object Loading : FetchedPageResult()
        class NoMoreDataAvailable(val allData: List<Item>) : FetchedPageResult()
    }

}