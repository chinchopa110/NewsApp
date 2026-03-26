package com.example.newsapp.data.repository

import com.example.newsapp.data.network.RetrofitClient
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.repository.ArticleRepository

class NetworkArticleRepository(private val apiKey: String) : ArticleRepository {

    override suspend fun getTopHeadlines(page: Int): Result<List<Article>> {
        return try {
            val response = RetrofitClient.newsApi.getTopHeadlines(country = "us", page = page, apiKey = apiKey)
            if (response.isSuccessful) {
                Result.Success(response.body()?.articles ?: emptyList())
            } else {
                Result.Error("Ошибка API: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Ошибка сети: ${e.message}")
        }
    }

    override suspend fun getEverything(query: String, page: Int): Result<List<Article>> {
        return try {
            val response = RetrofitClient.newsApi.getEverything(query = query, from = null, sortBy = "popularity", page = page, apiKey = apiKey)
            if (response.isSuccessful) {
                Result.Success(response.body()?.articles ?: emptyList())
            } else {
                Result.Error("Ошибка API: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Ошибка сети: ${e.message}")
        }
    }
}
