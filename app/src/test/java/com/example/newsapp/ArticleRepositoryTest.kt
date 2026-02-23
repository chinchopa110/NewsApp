package com.example.newsapp

import com.example.newsapp.data.repository.InMemoryArticleRepository
import com.example.newsapp.domain.model.Category
import com.example.newsapp.domain.model.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ArticleRepositoryTest {
    
    private lateinit var articleRepository: InMemoryArticleRepository
    
    @Before
    fun setup() {
        articleRepository = InMemoryArticleRepository()
    }
    
    @Test
    fun `getPublished should return only published articles`() = runBlocking {
        val result = articleRepository.getPublished()
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should have published articles", articles.isNotEmpty())
        
        articles.forEach { article ->
            assertTrue(
                "Article ${article.id} should be published",
                article.isPublished
            )
        }
        
        assertTrue("Should have at least 4 published articles", articles.size >= 4)
        
        val hasUnpublished = articles.any { !it.isPublished }
        assertFalse("Should not contain unpublished articles", hasUnpublished)
    }
    
    @Test
    fun `getByCategory should return articles of specific category`() = runBlocking {
        val category = Category.TECHNOLOGY
        
        val result = articleRepository.getByCategory(category)
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should have technology articles", articles.isNotEmpty())
        
        articles.forEach { article ->
            assertEquals(
                "Article ${article.id} should be in TECHNOLOGY category",
                category,
                article.category
            )
        }
    }
    
    @Test
    fun `getByAuthor should return articles by specific author`() = runBlocking {
        val authorId = "1"
        
        val result = articleRepository.getByAuthor(authorId)
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should have articles by author 1", articles.isNotEmpty())
        
        articles.forEach { article ->
            assertEquals(
                "Article ${article.id} should be by author $authorId",
                authorId,
                article.author.id
            )
        }
        
        assertTrue("Should have at least 2 articles by author 1", articles.size >= 2)
    }
    
    @Test
    fun `searchByTitle should find articles matching query`() = runBlocking {
        val query = "технологии"
        
        val result = articleRepository.searchByTitle(query)
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should find articles with 'технологии' in title", articles.isNotEmpty())
        
        articles.forEach { article ->
            assertTrue(
                "Article title should contain query",
                article.title.contains(query, ignoreCase = true)
            )
        }
    }
    
    @Test
    fun `searchByTitle with non-matching query should return empty list`() = runBlocking {
        val query = "несуществующая тема xyz123"
        
        val result = articleRepository.searchByTitle(query)
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should return empty list for non-matching query", articles.isEmpty())
    }
    
    @Test
    fun `getPremiumArticles should return only premium published articles`() = runBlocking {
        val result = articleRepository.getPremiumArticles()
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should have premium articles", articles.isNotEmpty())
        
        articles.forEach { article ->
            assertTrue(
                "Article ${article.id} should be premium",
                article.isPremium
            )
            assertTrue(
                "Article ${article.id} should be published",
                article.isPublished
            )
        }
    }
    
    @Test
    fun `getById should return specific article`() = runBlocking {
        val articleId = "1"
        
        val result = articleRepository.getById(articleId)
        
        assertTrue("Should return success", result is Result.Success)
        
        val article = (result as Result.Success).data
        assertEquals("Article ID should match", articleId, article.id)
        assertEquals("Article title should match", "Новые технологии в AI", article.title)
        assertEquals("Category should be TECHNOLOGY", Category.TECHNOLOGY, article.category)
    }
    
    @Test
    fun `getById with invalid id should return error`() = runBlocking {
        val invalidId = "999"
        
        val result = articleRepository.getById(invalidId)
        
        assertTrue("Should return error", result is Result.Error)
        
        val errorMessage = (result as Result.Error).message
        assertTrue(
            "Error message should mention article not found",
            errorMessage.contains("не найдена", ignoreCase = true)
        )
    }
    
    @Test
    fun `incrementViewCount should increase view count`() = runBlocking {
        val articleId = "1"
        val originalArticle = (articleRepository.getById(articleId) as Result.Success).data
        val originalViewCount = originalArticle.viewCount
        
        val result = articleRepository.incrementViewCount(articleId)
        
        assertTrue("Should return success", result is Result.Success)
        assertTrue("Should return true", (result as Result.Success).data)
        
        val updatedArticle = (articleRepository.getById(articleId) as Result.Success).data
        assertEquals(
            "View count should increase by 1",
            originalViewCount + 1,
            updatedArticle.viewCount
        )
    }
    
    @Test
    fun `incrementLikeCount should increase like count`() = runBlocking {
        val articleId = "2"
        val originalArticle = (articleRepository.getById(articleId) as Result.Success).data
        val originalLikeCount = originalArticle.likeCount
        
        val result = articleRepository.incrementLikeCount(articleId)
        
        assertTrue("Should return success", result is Result.Success)
        assertTrue("Should return true", (result as Result.Success).data)
        
        val updatedArticle = (articleRepository.getById(articleId) as Result.Success).data
        assertEquals(
            "Like count should increase by 1",
            originalLikeCount + 1,
            updatedArticle.likeCount
        )
    }
    
    @Test
    fun `getAll should return all articles including unpublished`() = runBlocking {
        val result = articleRepository.getAll()
        
        assertTrue("Should return success", result is Result.Success)
        
        val articles = (result as Result.Success).data
        assertTrue("Should have articles", articles.isNotEmpty())
        
        assertTrue("Should have at least 5 articles", articles.size >= 5)
        
        val hasPublished = articles.any { it.isPublished }
        val hasUnpublished = articles.any { !it.isPublished }
        
        assertTrue("Should have published articles", hasPublished)
        assertTrue("Should have unpublished articles", hasUnpublished)
    }
}