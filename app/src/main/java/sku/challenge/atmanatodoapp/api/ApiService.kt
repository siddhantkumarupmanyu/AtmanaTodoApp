package sku.challenge.atmanatodoapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import sku.challenge.atmanatodoapp.vo.FetchedPage

interface ApiService {

    @GET("/users")
    suspend fun getPage(@Query("page") pageNo: Int): FetchedPage

}