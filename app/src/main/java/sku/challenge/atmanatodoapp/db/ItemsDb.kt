package sku.challenge.atmanatodoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import sku.challenge.atmanatodoapp.vo.Item

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
abstract class ItemsDb : RoomDatabase() {

    abstract fun itemsDao(): ItemsDao

}