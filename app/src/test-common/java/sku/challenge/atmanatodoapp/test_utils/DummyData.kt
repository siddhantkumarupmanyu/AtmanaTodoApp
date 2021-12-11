package sku.challenge.atmanatodoapp.test_utils

import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item

object DummyData {

    fun items(startingId: Int, noOfItems: Int, page: Int): List<Item> {
        val items = (startingId..(startingId + noOfItems)).map { id: Int ->
            Item(
                id,
                "person${id}.page${page}@reqres.in",
                "Firstname${id}-${page}",
                "Lastname${id}-${page}"
            )
        }

        return items
    }

    fun fetchedPage(page: Int, startingId: Int, noOfItems: Int): FetchedPage {
        return FetchedPage(page, items(startingId, page, noOfItems))
    }

}