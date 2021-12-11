package sku.challenge.atmanatodoapp.repository

import sku.challenge.atmanatodoapp.vo.FetchedPage

interface ItemRepository {

    suspend fun fetchRemotePage(pageNo: Int): FetchedPage
}