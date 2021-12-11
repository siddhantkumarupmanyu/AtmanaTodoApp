package sku.challenge.atmanatodoapp.repository

import kotlinx.coroutines.flow.Flow
import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.db.ItemsDao
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item


// lack of better name
class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val dao: ItemsDao
) : ItemRepository {


    override suspend fun fetchRemotePage(pageNo: Int): FetchedPage {
        return apiService.getPage(pageNo)
    }

    override fun getLocalItems(): Flow<List<Item>> {
        return dao.getItems()
    }

    override suspend fun getItem(id: Int): Item {
        return dao.getItem(id)
    }

    override suspend fun saveLocalItem(item: Item) {
        dao.insertItems(item)
    }

    override suspend fun deleteLocalItem(item: Item) {
        dao.deleteItem(item)
    }

}