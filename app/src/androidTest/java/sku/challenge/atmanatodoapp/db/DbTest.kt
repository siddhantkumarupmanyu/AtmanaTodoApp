package sku.challenge.atmanatodoapp.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

abstract class DbTest {

    private lateinit var _db: ItemsDb

    val db: ItemsDb
        get() = _db

    @Before
    fun initDb() {
        _db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ItemsDb::class.java
        ).build()
    }

    @After
    fun closeDb() {
        _db.close()
    }

}