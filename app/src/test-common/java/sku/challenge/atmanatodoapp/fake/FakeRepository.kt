package sku.challenge.atmanatodoapp.fake

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.FetchedPage
import sku.challenge.atmanatodoapp.vo.Item

class FakeRepository : ItemRepository {
    var pageNo: Int = 0
    var delayBeforeReturningResult: Long = 0
    var fetchedPage: FetchedPage = FetchedPage(0, emptyList())

    var items: List<List<Item>> = emptyList()

    var item: Item = Item("", "", "", -1)

    var fetchRemotePageCalledTimes: Int = 0
        private set

    override suspend fun fetchRemotePage(pageNo: Int): FetchedPage {
        assertThat(pageNo, `is`(equalTo(this.pageNo)))

        delay(delayBeforeReturningResult)

        fetchRemotePageCalledTimes += 1

        return fetchedPage
    }

    override fun getLocalItems(): Flow<List<Item>> {
        return flowOf(*items.toTypedArray())
    }

    override suspend fun getItem(id: Int): Item {
        return item
    }

    override suspend fun saveLocalItem(item: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocalItem(item: Item) {
        TODO("Not yet implemented")
    }
}