package com.example.newsapp.data.network

import com.example.newsapp.domain.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("from") from: String?,
        @Query("sortBy") sortBy: String?,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String? = null,
        @Query("sources") sources: String? = null,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}