package com.leodeleon.data.remote

import com.leodeleon.data.BuildConfig
import com.leodeleon.domain.ApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {

    @GET("/v1/gifs/trending")
    fun getTrending(
        @Query("offset") offset: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY) : Single<ApiResponse>

    @GET("/v1/gifs/search")
    fun getSearch(
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY) : Single<ApiResponse>
}