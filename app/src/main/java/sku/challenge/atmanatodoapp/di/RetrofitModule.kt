package sku.challenge.atmanatodoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sku.challenge.atmanatodoapp.api.ApiService
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Singleton
    @Provides
    fun providesApiService(
        baseUrl: BaseUrl,
        client: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // not working
    // // https://stackoverflow.com/a/54056042
    // @Provides
    // fun providesIsInternetAvailable(client: OkHttpClient): suspend () -> Boolean {
    //     val checker = InternetChecker(BASE_URL, client)
    //     return checker::isInternetConnectionAvailable
    // }

}