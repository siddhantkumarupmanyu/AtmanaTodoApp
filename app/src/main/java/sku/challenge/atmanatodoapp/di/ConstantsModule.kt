package sku.challenge.atmanatodoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

const val BASE_URL = "https://reqres.in/api"

@Module
@InstallIn(SingletonComponent::class)
object ConstantsModule {

    @Provides
    fun baseUrl(): BaseUrl {
        return BaseUrl(BASE_URL)
    }

}

data class BaseUrl(val baseUrl: String)