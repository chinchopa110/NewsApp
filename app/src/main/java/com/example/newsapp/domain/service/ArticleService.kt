package com.example.newsapp.domain.service

import com.example.newsapp.domain.model.*
import com.example.newsapp.domain.repository.ArticleRepository

class ArticleService(
    private val articleRepository: ArticleRepository
) : BaseService() {
    
    override fun getServiceName(): String = "ArticleService"
    suspend fun getArticleById(articleId: String): Result<Article> {
        logOperation("Получение статьи по ID: $articleId")
        
        if (!validateNotEmpty(articleId, "articleId")) {
            return Result.Error("ID статьи не может быть пустым")
        }
        
        return articleRepository.getById(articleId)
    }
    
    suspend fun getPublishedArticles(): Result<List<Article>> {
        logOperation("Получение опубликованных статей")
        return articleRepository.getPublished()
    }
    
    suspend fun getArticlesByCategory(category: Category): Result<List<Article>> {
        logOperation("Получение статей по категории: ${category.displayName}")
        return articleRepository.getByCategory(category)
    }
    
    
    suspend fun processArticleResult(result: Result<Article>): String {
        return when (result) {
            is Result.Success -> {
                logOperation("Статья успешно получена")
                "Статья: ${result.data.title}"
            }
            is Result.Error -> {
                logOperation("Ошибка получения статьи")
                "Ошибка: ${result.message}"
            }
        }
    }
    
    fun validateArticle(article: Any): Boolean {
        if (article !is Article) {
            println("Ошибка: объект не является статьей")
            return false
        }
        return validateNotEmpty(article.title, "title") && 
               validateNotEmpty(article.content, "content")
    }
    
    fun tryConvertToArticle(obj: Any): Article? {
        val article = obj as? Article
        if (article == null) {
            println("Не удалось преобразовать объект в Article")
            return null
        }
        return article
    }
    
    fun getArticleTitle(article: Article?): String {
        return article!!.title
    }
    
    fun filterArticlesByCategory(articles: List<Article>, category: Category): List<Article> {
        logOperation("Фильтрация статей по категории: ${category.displayName}")
        return articles.filter { it.category == category }
    }
    
    fun getTopArticles(articles: Array<Article>, count: Int): Array<Article> {
        return articles
            .sortedByDescending { it.viewCount }
            .take(count)
            .toTypedArray()
    }
    
    fun getArticleTitles(articles: List<Article>): List<String> {
        return articles.map { it.title }
    }
}