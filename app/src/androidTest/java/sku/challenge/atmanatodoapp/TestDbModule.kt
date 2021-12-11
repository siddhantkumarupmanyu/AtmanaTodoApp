package sku.challenge.atmanatodoapp

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import sku.challenge.atmanatodoapp.db.ItemsDao
import sku.challenge.atmanatodoapp.db.ItemsDb
import sku.challenge.atmanatodoapp.di.DbModule
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DbModule::class]
)
object TestDbModule {

    @Singleton
    @Provides
    fun providesDb(app: Application): ItemsDb {
        return Room.inMemoryDatabaseBuilder(
            app,
            ItemsDb::class.java
        ).build()
    }

    @Singleton
    @Provides
    fun providesTrackerDb(db: ItemsDb): ItemsDao {
        return db.itemsDao()
    }

}