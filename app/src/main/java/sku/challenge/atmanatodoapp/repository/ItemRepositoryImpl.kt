package sku.challenge.atmanatodoapp.repository

import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.vo.FetchedPage


// lack of better name
class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {


    override suspend fun fetchRemotePage(pageNo: Int): FetchedPage {
        return apiService.getPage(pageNo)
    }

}