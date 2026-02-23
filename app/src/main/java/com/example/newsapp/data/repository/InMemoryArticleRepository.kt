package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Author
import com.example.newsapp.domain.model.Category
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.repository.ArticleRepository
import java.util.Date

class InMemoryArticleRepository : ArticleRepository {
    
    private val articles = mutableMapOf<String, Article>()
    
    init {
        val testArticles = listOf(
            Article(
                id = "1",
                title = "Новые технологии в AI",
                content = "Искусственный интеллект продолжает развиваться...",
                summary = "Обзор последних достижений в области AI",
                author = Author(
                    id = "1",
                    name = "John Doe",
                    bio = "Технический журналист",
                    avatarUrl = "https://example.com/avatars/john.jpg"
                ),
                category = Category.TECHNOLOGY,
                tags = listOf("AI", "технологии", "инновации"),
                imageUrl = "https://example.com/images/ai.jpg",
                publishedAt = Date(),
                updatedAt = null,
                viewCount = 150,
                likeCount = 25,
                isPublished = true,
                isPremium = false
            ),
            Article(
                id = "2",
                title = "Чемпионат мира по футболу",
                content = "Сборная России вышла в финал...",
                summary = "Результаты последних матчей",
                author = Author(
                    id = "2",
                    name = "Jane Smith",
                    bio = "Спортивный обозреватель",
                    avatarUrl = "https://example.com/avatars/jane.jpg"
                ),
                category = Category.SPORTS,
                tags = listOf("футбол", "спорт", "чемпионат"),
                imageUrl = "https://example.com/images/football.jpg",
                publishedAt = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                updatedAt = null,
                viewCount = 320,
                likeCount = 45,
                isPublished = true,
                isPremium = false
            ),
            Article(
                id = "3",
                title = "Экономический прогноз на 2026 год",
                content = "Эксперты прогнозируют рост экономики...",
                summary = "Анализ экономической ситуации",
                author = Author(
                    id = "1",
                    name = "John Doe",
                    bio = "Технический журналист",
                    avatarUrl = "https://example.com/avatars/john.jpg"
                ),
                category = Category.BUSINESS,
                tags = listOf("экономика", "бизнес", "прогноз"),
                imageUrl = "https://example.com/images/economy.jpg",
                publishedAt = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                updatedAt = null,
                viewCount = 200,
                likeCount = 30,
                isPublished = true,
                isPremium = true
            ),
            Article(
                id = "4",
                title = "Новое открытие в медицине",
                content = "Ученые разработали новый метод лечения...",
                summary = "Прорыв в медицинских исследованиях",
                author = Author(
                    id = "3",
                    name = "Dr. Smith",
                    bio = "Медицинский эксперт",
                    avatarUrl = null
                ),
                category = Category.HEALTH,
                tags = listOf("медицина", "здоровье", "наука"),
                imageUrl = "https://example.com/images/medicine.jpg",
                publishedAt = Date(),
                updatedAt = null,
                viewCount = 100,
                likeCount = 15,
                isPublished = true,
                isPremium = false
            ),
            Article(
                id = "5",
                title = "Черновик статьи",
                content = "Это черновик, который еще не опубликован...",
                summary = "Тестовый черновик",
                author = Author(
                    id = "1",
                    name = "John Doe",
                    bio = "Технический журналист",
                    avatarUrl = "https://example.com/avatars/john.jpg"
                ),
                category = Category.TECHNOLOGY,
                tags = listOf("тест"),
                imageUrl = null,
                publishedAt = Date(),
                updatedAt = null,
                viewCount = 0,
                likeCount = 0,
                isPublished = false,
                isPremium = false
            )
        )
        
        testArticles.forEach { articles[it.id] = it }
    }
    
    override suspend fun getById(id: String): Result<Article> {
        return articles[id]?.let { Result.Success(it) }
            ?: Result.Error("Статья с ID $id не найдена")
    }
    
    override suspend fun getAll(): Result<List<Article>> {
        return Result.Success(articles.values.toList())
    }
    
    override suspend fun save(entity: Article): Result<Article> {
        articles[entity.id] = entity
        return Result.Success(entity)
    }
    
    override suspend fun delete(id: String): Result<Boolean> {
        return if (articles.remove(id) != null) {
            Result.Success(true)
        } else {
            Result.Error("Статья с ID $id не найдена")
        }
    }
    
    override suspend fun update(entity: Article): Result<Article> {
        return if (articles.containsKey(entity.id)) {
            articles[entity.id] = entity
            Result.Success(entity)
        } else {
            Result.Error("Статья с ID ${entity.id} не найдена")
        }
    }
    
    override suspend fun getByCategory(category: Category): Result<List<Article>> {
        val filtered = articles.values.filter { it.category == category }
        return Result.Success(filtered)
    }
    
    override suspend fun getByAuthor(authorId: String): Result<List<Article>> {
        val filtered = articles.values.filter { it.author.id == authorId }
        return Result.Success(filtered)
    }
    
    override suspend fun searchByTitle(query: String): Result<List<Article>> {
        val filtered = articles.values.filter { 
            it.title.contains(query, ignoreCase = true) 
        }
        return Result.Success(filtered)
    }
    
    override suspend fun getPublished(): Result<List<Article>> {
        val filtered = articles.values.filter { it.isPublished }
        return Result.Success(filtered)
    }
    
    override suspend fun getPremiumArticles(): Result<List<Article>> {
        val filtered = articles.values.filter { it.isPremium && it.isPublished }
        return Result.Success(filtered)
    }
    
    override suspend fun incrementViewCount(articleId: String): Result<Boolean> {
        val article = articles[articleId]
        return if (article != null) {
            articles[articleId] = article.copy(viewCount = article.viewCount + 1)
            Result.Success(true)
        } else {
            Result.Error("Статья с ID $articleId не найдена")
        }
    }
    
    override suspend fun incrementLikeCount(articleId: String): Result<Boolean> {
        val article = articles[articleId]
        return if (article != null) {
            articles[articleId] = article.copy(likeCount = article.likeCount + 1)
            Result.Success(true)
        } else {
            Result.Error("Статья с ID $articleId не найдена")
        }
    }
}