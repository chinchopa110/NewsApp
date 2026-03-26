package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result

interface ArticleRepository {
    suspend fun getTopHeadlines(page: Int): Result<List<Article>>
    suspend fun getEverything(query: String, page: Int): Result<List<Article>>
}
