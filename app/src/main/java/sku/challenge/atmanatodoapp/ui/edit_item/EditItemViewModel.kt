package sku.challenge.atmanatodoapp.ui.edit_item

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
    // consider making it only for one time consumption
    // follow/implement one time event pattern

    // YAGNI for now and just making it a save event rather than generic event
    // Is considering to add test so it is only consumed once

    private val _event: MutableStateFlow<Event> = MutableStateFlow(Event.InitialFlowEvent)
    val event: StateFlow<Event> = _event

    // this code Item("", "", "", 0) is repeated so many times
    // i should consider applying null object pattern i.e. making static EMPTY_ITEM in Item Class
    private var item = Item("", "", "", 0)

    fun loadItem(id: Int) {
        // fail fast;
        assert((id != -1) || (id != 0))

        viewModelScope.launch {
            item = repository.getItem(id)
            _event.value = Event.ItemEvent(item)
        }
    }

    fun saveItem(email: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            repository.saveLocalItem(
                item.copy(
                    email = email,
                    firstName = firstName,
                    lastName = lastName
                )
            )
            _event.value = Event.SaveEvent
        }
    }

    sealed class Event {
        data class ItemEvent(val item: Item) : Event()
        object SaveEvent : Event()
        object InitialFlowEvent : Event()
    }
}