package com.toi.weathetapp

import com.toi.weathetapp.network.WebApiInterface
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class WeatherAPITest {

    private var mockWebServer = MockWebServer()
    private lateinit var webApiInterface: WebApiInterface


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        webApiInterface =
            getRetrofitBuilderTesting(mockWebServer).create(WebApiInterface::class.java)
    }

    private fun getRetrofitBuilderTesting(mockWebServer: MockWebServer): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build()).build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testWeatherDataSuccess() = runBlocking {
        val responseString =
            this.javaClass.classLoader!!.getResource("weather.json").readText()

        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseString)
        mockWebServer.enqueue(mockResponse)
        val result = webApiInterface.getCurrentLocationTemp(
            "532d06c031a2964db3ff303778b1f362",
            "Uttar Pradesh"
        )

        TestCase.assertNotNull(result)
        TestCase.assertNotNull(result.current)
    }

}