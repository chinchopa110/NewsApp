package com.example.newsapp

import com.example.newsapp.data.repository.InMemoryArticleRepository
import com.example.newsapp.domain.model.Category
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.service.ArticleService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ArticleServiceTest {
    
    private lateinit var articleRepository: InMemoryArticleRepository
    private lateinit var articleService: ArticleService
    
    @Before
    fun setup() {
        articleRepository = InMemoryArticleRepository()
        articleService = ArticleService(articleRepository)
    }
    
    @Test
    fun `getArticleById with valid id should return article`() = runBlocking {
        val articleId = "1"
        
        val result = articleService.getArticleById(articleId)
        
        assertTrue(result is Result.Success)
        val article = (result as Result.Success).data
        assertEquals(articleId, article.id)
        assertEquals("Новые технологии в AI", article.title)
        assertEquals(Category.TECHNOLOGY, article.category)
    }
    
    @Test
    fun `getArticleById with invalid id should return error`() = runBlocking {
        val invalidId = "999"
        
        val result = articleService.getArticleById(invalidId)
        
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).message
        assertTrue(errorMessage.contains("не найдена", ignoreCase = true))
    }
    
    @Test
    fun `getArticleById with empty id should return error`() = runBlocking {
        val emptyId = ""
        
        val result = articleService.getArticleById(emptyId)
        
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).message
        assertTrue(errorMessage.contains("не может быть пустым", ignoreCase = true))
    }
    
    @Test
    fun `getPublishedArticles should return only published articles`() = runBlocking {
        val result = articleService.getPublishedArticles()
        
        assertTrue(result is Result.Success)
        val articles = (result as Result.Success).data
        assertTrue(articles.isNotEmpty())
        articles.forEach { article ->
            assertTrue(article.isPublished)
        }
    }
    
    @Test
    fun `getArticlesByCategory should return articles of specific category`() = runBlocking {
        val category = Category.SPORTS
        
        val result = articleService.getArticlesByCategory(category)
        
        assertTrue(result is Result.Success)
        val articles = (result as Result.Success).data
        assertTrue(articles.isNotEmpty())
        articles.forEach { article ->
            assertEquals(category, article.category)
        }
    }
}