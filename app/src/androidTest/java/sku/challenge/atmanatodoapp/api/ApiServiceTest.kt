package sku.challenge.atmanatodoapp.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sku.challenge.atmanatodoapp.test_utils.enqueueResponse
import sku.challenge.atmanatodoapp.vo.Item
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    private val mockWebServer = MockWebServer()

    private lateinit var service: ApiService

    @Before
    fun setUp() {
        mockWebServer.start(8080)

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchPage() = runTest {
        mockWebServer.enqueueResponse("page2.json")

        val fetchedPage = service.getPage(2)

        val request = mockWebServer.takeRequest(2, TimeUnit.SECONDS)
        assertThat(request.path, `is`("/users?page=2"))

        assertThat(fetchedPage.page, `is`(2))

        val items = fetchedPage.data


        assertItem(
            items[0],
            7,
            "michael.lawson@reqres.in",
            "Michael",
            "Lawson"
        )

        assertItem(
            items[3],
            10,
            "byron.fields@reqres.in",
            "Byron",
            "Fields"
        )

    }

    @Test
    fun shouldHaveEmptyList_WhenThereIsNoData() = runTest {
        mockWebServer.enqueueResponse("page3.json")

        val fetchedPage = service.getPage(3)

        val request = mockWebServer.takeRequest(2, TimeUnit.SECONDS)
        assertThat(request.path, `is`("/users?page=3"))

        assertThat(fetchedPage.page, `is`(3))

        assertThat(fetchedPage.data, `is`(emptyList()))
    }

    private fun assertItem(
        item: Item,
        id: Int,
        email: String,
        firstName: String,
        lastName: String
    ) {
        assertThat(item, `is`(equalTo(Item(email, firstName, lastName, id))))
    }

}