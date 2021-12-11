package sku.challenge.atmanatodoapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sku.challenge.atmanatodoapp.db.ItemsDao
import sku.challenge.atmanatodoapp.db.ItemsDb
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun providesDb(app: Application): ItemsDb {
        return Room.databaseBuilder(app, ItemsDb::class.java, "tracks.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesTrackerDao(db: ItemsDb): ItemsDao {
        return db.itemsDao()
    }

}