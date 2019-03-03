package com.leodeleon.data.remote

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class GiphyApi {

    private companion object {
        const val BASE_URL = "https://api.giphy.com"
    }

    val service: GiphyService
    private val client = provideOkHttp()

    init {
        service = provideService(client)
    }

    private fun provideOkHttp() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private inline fun <reified T> provideService(okHttp: OkHttpClient): T {
        val retrofit =
            Retrofit.Builder()
                .client(okHttp)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .build()

        return retrofit.create(T::class.java)
    }
}