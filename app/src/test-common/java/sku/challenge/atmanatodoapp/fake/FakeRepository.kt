package sku.challenge.atmanatodoapp.fake

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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

    var fetchRemotePageCalledTimes: Int = 0
        private set

    override suspend fun fetchRemotePage(pageNo: Int): FetchedPage {
        assertThat(pageNo, `is`(equalTo(this.pageNo)))

        delay(delayBeforeReturningResult)

        fetchRemotePageCalledTimes += 1

        return fetchedPage
    }

    override fun getLocalItems(): Flow<List<Item>> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(id: Int): Item {
        TODO("Not yet implemented")
    }
}