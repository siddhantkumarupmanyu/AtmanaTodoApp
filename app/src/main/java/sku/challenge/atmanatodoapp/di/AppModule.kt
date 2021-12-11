package sku.challenge.atmanatodoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sku.challenge.atmanatodoapp.api.ApiService
import sku.challenge.atmanatodoapp.repository.ItemRepository
import sku.challenge.atmanatodoapp.repository.ItemRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        service: ApiService,
    ): ItemRepository {
        return ItemRepositoryImpl(
            service
        )
    }


}