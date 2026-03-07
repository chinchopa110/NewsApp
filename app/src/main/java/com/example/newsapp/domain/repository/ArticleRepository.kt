package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result

interface ArticleRepository {
    suspend fun getAll(): Result<List<Article>>
    suspend fun getPublished(): Result<List<Article>>
    suspend fun searchByTitle(query: String): Result<List<Article>>
}