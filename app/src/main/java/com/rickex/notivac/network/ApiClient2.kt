package com.rickex.notivac.network

import com.google.gson.GsonBuilder
import com.tcp.rickexdriver.network.apiKotlin
import com.tcp.rickexuser.preferences.UserPreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient2 {
    private val retrofit: Retrofit? = null
    private val gson = GsonBuilder()
            .setLenient()
            .create()


    val client1: Retrofit //no authorization in header
        get() = Retrofit.Builder()
                .baseUrl(apiKotlin.BASE_URL)
                .client(header1)
                .addConverterFactory(GsonConverterFactory.create(gson)) //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    // Request customization: add request headers
    val header1: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor { chain: Interceptor.Chain ->
                        var request: Request? = null
                        val original = chain.request()
                        // Request customization: add request headers
                        val requestBuilder = original.newBuilder()
                                //.addHeader("Authorization", UserPreferenceManager.getAuthToken(MyApplication.appContext!!))
                        request = requestBuilder.build()
                        chain.proceed(request!!)
                    }
                    .build()


        }

    val client: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(apiKotlin.BASE_URL)
                .client(header)
                .addConverterFactory(GsonConverterFactory.create(gson)) //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    val header: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor { chain: Interceptor.Chain ->
                        var request: Request? = null
                        val original = chain.request()
                        // Request customization: add request headers
                        //'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36',
                        val requestBuilder = original.newBuilder()
                                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        request = requestBuilder.build()
                        chain.proceed(request!!)
                    }
                    .build()


        }
}