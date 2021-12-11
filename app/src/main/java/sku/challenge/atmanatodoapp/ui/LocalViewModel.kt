package sku.challenge.atmanatodoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import sku.challenge.atmanatodoapp.repository.ItemRepository
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    val items =
        repository.getLocalItems().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}