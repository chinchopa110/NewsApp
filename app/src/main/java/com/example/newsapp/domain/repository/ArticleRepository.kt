package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Category
import com.example.newsapp.domain.model.Result

interface ArticleRepository : Repository<Article, String> {
    suspend fun getByCategory(category: Category): Result<List<Article>>
    suspend fun getByAuthor(authorId: String): Result<List<Article>>
    suspend fun searchByTitle(query: String): Result<List<Article>>
    suspend fun getPublished(): Result<List<Article>>
    suspend fun getPremiumArticles(): Result<List<Article>>
    suspend fun incrementViewCount(articleId: String): Result<Boolean>
    suspend fun incrementLikeCount(articleId: String): Result<Boolean>
}