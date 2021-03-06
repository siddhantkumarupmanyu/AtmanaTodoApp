package sku.challenge.atmanatodoapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sku.challenge.atmanatodoapp.vo.Item

@Dao
abstract class ItemsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItems(vararg item: Item)

    @Query("SELECT * FROM item")
    abstract fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id = :id")
    abstract suspend fun getItem(id: Int): Item

    @Delete
    abstract suspend fun deleteItem(item: Item)

}