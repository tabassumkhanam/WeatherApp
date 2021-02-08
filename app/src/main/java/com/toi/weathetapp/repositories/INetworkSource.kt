package com.toi.weathetapp.repositories

import com.toi.weathetapp.model.ResultData
import com.toi.weathetapp.network.Resource

interface INetworkSource {

    suspend fun getCurrentLocationTemp(accessKey: String, query: String): Resource<ResultData>
}