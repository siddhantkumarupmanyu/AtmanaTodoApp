package sku.challenge.atmanatodoapp.fake

import kotlinx.coroutines.delay
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.vo.FetchedPage

class FakeRepository : ItemRepository {
    var pageNo: Int = 0
    var delayBeforeReturningResult: Long = 0
    var fetchedPage: FetchedPage = FetchedPage(0, emptyList())

    override suspend fun fetchRemotePage(pageNo: Int): FetchedPage {
        assertThat(pageNo, `is`(equalTo(this.pageNo)))

        delay(delayBeforeReturningResult)

        return fetchedPage
    }
}