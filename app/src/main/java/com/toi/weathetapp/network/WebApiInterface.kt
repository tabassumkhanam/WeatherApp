package com.toi.weathetapp.network

import com.toi.weathetapp.model.ResultData
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApiInterface {
    @GET("current")
    suspend fun getCurrentLocationTemp(
        @Query("access_key") accessKey: String,
        @Query("query") query: String
    ): ResultData

}