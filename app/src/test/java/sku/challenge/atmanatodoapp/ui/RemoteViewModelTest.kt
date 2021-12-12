package sku.challenge.atmanatodoapp.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import sku.challenge.atmanatodoapp.fake.FakeRepository
import sku.challenge.atmanatodoapp.test_utils.DummyData
import sku.challenge.atmanatodoapp.ui.remote.RemoteViewModel
import sku.challenge.atmanatodoapp.vo.FetchedPage

@ExperimentalCoroutinesApi
class RemoteViewModelTest {

    private val fakeRepository = FakeRepository()

    private val viewModel = RemoteViewModel(fakeRepository)


    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchNextPage() = runTest {
        assertThat(
            (viewModel.items.first() as RemoteViewModel.FetchedPageResult.Success).data,
            `is`(emptyList())
        )

        fakeRepository.delayBeforeReturningResult = 10L
        fakeRepository.pageNo = 1
        fakeRepository.fetchedPage = DummyData.fetchedPage(1, 1, 6)

        viewModel.fetchNextPage()

        yield()
        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Loading::class.java)
        )

        // more delay relative to delayBeforeReturningResult, so that's run first
        delay(20L)

        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Success::class.java)
        )

        val items = (viewModel.items.first() as RemoteViewModel.FetchedPageResult.Success).data

        assertThat(items, `is`(equalTo(DummyData.items(1, 6, 1))))

        fakeRepository.pageNo = 2
        fakeRepository.fetchedPage = DummyData.fetchedPage(2, 7, 6)

        viewModel.fetchNextPage()

        yield()
        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Loading::class.java)
        )

        delay(20L)

        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Success::class.java)
        )

        val itemsCombined =
            (viewModel.items.first() as RemoteViewModel.FetchedPageResult.Success).data

        assertThat(itemsCombined.size, `is`(equalTo(12)))
        assertThat(
            itemsCombined,
            `is`(equalTo(DummyData.items(1, 6, 1) + DummyData.items(7, 6, 2)))
        )
    }

    @Test
    fun shouldNotFetchNextPage_WhenPreviousPageHasNoData() = runTest {
        fakeRepository.delayBeforeReturningResult = 10L
        fakeRepository.pageNo = 1
        fakeRepository.fetchedPage = FetchedPage(1, emptyList())

        viewModel.fetchNextPage()
        yield()
        delay(20L)

        val items =
            (viewModel.items.first() as RemoteViewModel.FetchedPageResult.NoMoreDataAvailable).allData
        assertThat(items, `is`(emptyList()))

        viewModel.fetchNextPage()
        yield()

        assertThat(
            "should be called only once, for page 1",
            fakeRepository.fetchRemotePageCalledTimes,
            `is`(equalTo(1))
        )
    }

    @Test
    fun fetchRemotePageShowBeSynchronized() = runTest {
        // i can't thing of a better name for this test right now

        fakeRepository.delayBeforeReturningResult = 10L
        fakeRepository.pageNo = 1
        fakeRepository.fetchedPage = DummyData.fetchedPage(1, 1, 6)
        fakeRepository.assertPage = false

        viewModel.fetchNextPage()
        yield()
        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Loading::class.java)
        )

        viewModel.fetchNextPage()
        viewModel.fetchNextPage()
        viewModel.fetchNextPage()

        // virtual delay so it's run after all the fetchNextPage
        delay(100L)
        assertThat(
            viewModel.items.first(),
            IsInstanceOf(RemoteViewModel.FetchedPageResult.Success::class.java)
        )
    }
}