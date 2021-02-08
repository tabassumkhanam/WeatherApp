package com.toi.weathetapp.repositories

import com.toi.weathetapp.model.ResultData
import com.toi.weathetapp.network.Resource
import com.toi.weathetapp.network.WebApiInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class NetworkSourceImpl @Inject constructor(
    @Named("IO") private var dispatcher: CoroutineDispatcher,
    private val webApi: WebApiInterface
) : INetworkSource {

    override suspend fun getCurrentLocationTemp(
        accessKey: String,
        query: String
    ): Resource<ResultData> {
        return safeApiCall(dispatcher) {
            webApi.getCurrentLocationTemp(accessKey, query)
        }
    }

    suspend inline fun <reified T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        noinline apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(dispatcher) {
            try {
                Resource.success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.error(throwable, null)
            }
        }
    }
}