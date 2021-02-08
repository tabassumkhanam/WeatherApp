package com.toi.weathetapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toi.weathetapp.model.ResultData
import com.toi.weathetapp.network.Resource
import com.toi.weathetapp.repositories.INetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class WeatherViewModel @Inject constructor(
    @Named("IO")
    private var ioDispatcher: CoroutineDispatcher,
    @Named("MAIN") private var mainDispatcher: CoroutineDispatcher,
    private var repository: INetworkSource
) : ViewModel() {

    private val weatherLiveData = MutableLiveData<Resource<ResultData>?>()

    fun getWeatherLiveData(): MutableLiveData<Resource<ResultData>?> {
        return weatherLiveData
    }

    fun getWeatherDataForCurrentLocation(accessKey: String, query: String) {
        weatherLiveData.postValue(Resource.loading(null))
        val headerMap = HashMap<String, Any>()
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val responseData = repository.getCurrentLocationTemp(accessKey, query)
                weatherLiveData.postValue(responseData)
            }
        }
    }

}