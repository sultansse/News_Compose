package com.softwareit.newscompose.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HackerNewsAPI {
    @GET("item/{id}.json")
    suspend fun getItem(@Path("id") id: String): NewsItem

    @GET("item")
    suspend fun getItems(@Query("id") ids: String): List<NewsItem>
}

