package com.toi.weathetapp.network

import com.toi.weathetapp.model.ResultData
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApiInterface {
    @GET("current")
    fun getCurrentLocationTemp(
        @Query("access_key") accessKey: String,
        @Query("query") query: String
    ): ResultData

}