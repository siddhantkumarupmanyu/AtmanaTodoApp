package sku.challenge.atmanatodoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.Item
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {
    private val _item = MutableStateFlow(Item("", "", "", -1))

    val item: StateFlow<Item> = _item

    // consider making it only for one time consumption
    // follow/implement one time event pattern

    // YAGNI for now and just making it a save event rather than generic event
    // Is considering to add test so it is only consumed once

    private val _saveEvent: MutableStateFlow<Any> = MutableStateFlow(Any())
    val saveEvent: StateFlow<Any> = _saveEvent

    fun loadItem(id: Int) {
        viewModelScope.launch {
            _item.value = repository.getItem(id)
        }
    }

    fun saveItem(email: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            repository.saveLocalItem(
                item.value.copy(
                    email = email,
                    firstName = firstName,
                    lastName = lastName
                )
            )

            _saveEvent.value = SaveEvent()
        }
    }

    class SaveEvent

}