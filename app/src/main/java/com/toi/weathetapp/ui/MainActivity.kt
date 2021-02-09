package com.toi.weathetapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import com.toi.weathetapp.R
import com.toi.weathetapp.di.ViewModelFactory
import com.toi.weathetapp.model.ResultData
import com.toi.weathetapp.network.Resource
import com.toi.weathetapp.ui.viewmodel.WeatherViewModel
import com.toi.weathetapp.utils.*
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private val LOCATION__PERMISSION_REQUEST_CODE = 3

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var weatherViewModel: WeatherViewModel

    @Inject
    lateinit var internetConnectionManager: InternetConnectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reqPermissionForLocation()
    }

    private fun reqPermissionForLocation() {
        this?.let { activity ->
            when {
                PermissionUtils.isAccessFineLocationGranted(activity) -> {
                    when {
                        PermissionUtils.isLocationEnabled(activity) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(activity)
                        }
                    }
                }
                else -> {
                    PermissionUtils.requestAccessFineLocationPermission(
                        activity,
                        LOCATION__PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionUtils.requestAccessFineLocationPermission(
                this,
                LOCATION__PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                getState(it.latitude, it.longitude)
            }
        }
    }

    private fun getState(lat: Double, long: Double) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> =
                geoCoder.getFromLocation(lat, long, 1)
            if (addresses.isNotEmpty()) {
                tvLocation.text = addresses[0].adminArea
            }
        } catch (e: IOException) {

        }
        initialize()
        subscribeObservers()
    }


    private fun initialize() {
        weatherViewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)
        handleApiCall()
        setListener()

    }

    private fun handleApiCall() {
        if (internetConnectionManager.isNetworkAvailable()) {
            weatherViewModel.getWeatherDataForCurrentLocation(
                getString(R.string.app_key),
                tvLocation.text.toString()
            )
        } else {
            showLongToast(getString(R.string.internet_connection_error_message))
        }
    }

    private fun setListener() {
        btnRetry.setOnClickListener {
            handleApiCall()
        }
    }

    private fun subscribeObservers() {
        weatherViewModel.getWeatherLiveData()
            .observe(this, androidx.lifecycle.Observer { handleResult(it) })
    }


    private fun handleResult(response: Resource<ResultData>?) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                showLoading(true)
            }
            Resource.Status.SUCCESS -> {
                showLoading(false)
                handleResultData(response.data)
            }
            Resource.Status.ERROR -> {
                showLoading(false)
                inclError.visible()
            }
        }
    }

    private fun handleResultData(data: ResultData?) {
        data?.let { result ->
            tvTemp.text = result.current.temperature.toString() ?: ""
            tvWindValue.text = result.current.wind_speed.toString() ?: ""
            tvPressureValue.text = result.current.pressure.toString() ?: ""
            tvPreciptValue.text = result.current.precip.toString() ?: ""
            tvCloudValue.text = result.current.cloudcover.toString() ?: ""
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            inclLoader.visible()
        } else {
            inclLoader.gone()
        }
    }
}