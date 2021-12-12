package sku.challenge.atmanatodoapp.ui.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.Item
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    val items =
        repository.getLocalItems().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteLocalItem(item)
        }
    }

}