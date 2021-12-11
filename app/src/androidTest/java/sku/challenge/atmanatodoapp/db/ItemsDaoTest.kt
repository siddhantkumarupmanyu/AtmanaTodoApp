package sku.challenge.atmanatodoapp.db

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsNull.nullValue
import org.junit.Test
import sku.challenge.atmanatodoapp.vo.Item


// tests should never extend, every test should be decoupled from other
// ignoring this one for now
@ExperimentalCoroutinesApi
class ItemsDaoTest : DbTest() {


    private val itemsDao
        get() = db.itemsDao()


    @Test
    fun insertItem() = runTest {
        val item1 = Item("1@eg.com", "f1", "l1")
        val item2 = Item("2@eg.com", "f2", "l2")

        itemsDao.insertItems(item1, item2)

        val retrievedItems = itemsDao.getItems().first()

        assertThat(retrievedItems, `is`(equalTo(listOf(item1.copy(id = 1), item2.copy(id = 2)))))
    }

    @Test
    fun getItemById() = runTest {
        val item1 = Item("1@eg.com", "f1", "l1")
        val item2 = Item("2@eg.com", "f2", "l2")

        itemsDao.insertItems(item1, item2)

        assertThat(itemsDao.getItem(1), `is`(equalTo(item1.copy(id = 1))))
    }

    @Test
    fun editAndDeleteItem() = runTest {
        val item1 = Item("1@eg.com", "f1", "l1")
        itemsDao.insertItems(item1)

        assertThat(itemsDao.getItem(1), `is`(equalTo(item1.copy(id = 1))))

        val editedItem = item1.copy(firstName = "firstName", id = 1)

        itemsDao.insertItems(editedItem)
        assertThat(itemsDao.getItem(1), `is`(equalTo(editedItem)))

        itemsDao.deleteItem(editedItem)
        assertThat(itemsDao.getItem(1), `is`(nullValue()))
    }

}