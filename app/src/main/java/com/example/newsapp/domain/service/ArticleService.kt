package com.example.newsapp.domain.service

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.repository.ArticleRepository

class ArticleService(
    private val articleRepository: ArticleRepository
) : BaseService() {
    
    override fun getServiceName(): String = "ArticleService"

    suspend fun getPublishedArticles(): Result<List<Article>> {
        logOperation("Получение опубликованных статей")
        return articleRepository.getPublished()
    }
    
    suspend fun searchArticles(query: String): Result<List<Article>> {
        logOperation("Поиск статей по заголовку: $query")
        return articleRepository.searchByTitle(query)
    }

    fun validateArticle(article: Article): Boolean {
        return validateNotEmpty(article.title, "title") && 
               validateNotEmpty(article.content, "content")
    }
}