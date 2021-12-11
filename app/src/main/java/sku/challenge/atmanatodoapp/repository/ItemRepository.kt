package sku.challenge.atmanatodoapp.repository

import kotlinx.coroutines.flow.Flow
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item

interface ItemRepository {

    suspend fun fetchRemotePage(pageNo: Int): FetchedPage

    fun getLocalItems(): Flow<List<Item>>

    suspend fun getItem(id: Int): Item

    suspend fun saveLocalItem(item: Item)

    suspend fun deleteLocalItem(item: Item)
}