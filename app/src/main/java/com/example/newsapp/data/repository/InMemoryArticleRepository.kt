package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.Source
import com.example.newsapp.domain.repository.ArticleRepository

class InMemoryArticleRepository : ArticleRepository {
    
    private val articles = listOf(
        Article(
            source = Source(id = null, name = "Pypi.org"),
            author = "jason@jasonacox.com",
            title = "pypowerwall-server 0.2.1",
            description = "A high-performance FastAPI-based server for monitoring and managing Tesla Powerwall systems",
            url = "https://pypi.org/project/pypowerwall-server/0.2.1/",
            urlToImage = null,
            publishedAt = "2026-03-05T06:56:27Z",
            content = "A high-performance FastAPI-based server for monitoring and managing Tesla Powerwall systems. This library provides a simple interface to interact with Powerwall gateways.",
            isPublished = true
        ),
        Article(
            source = Source(id = "tech-crunch", name = "TechCrunch"),
            author = "Jane Doe",
            title = "The Future of AI in Mobile Apps",
            description = "AI is changing how we interact with our phones every day.",
            url = "https://techcrunch.com/ai-future",
            urlToImage = null,
            publishedAt = "2026-03-04T10:00:00Z",
            content = "Artificial intelligence is no longer a futuristic concept. It's integrated into every aspect of mobile app development, from personalized recommendations to advanced image processing.",
            isPublished = true
        ),
        Article(
            source = Source(id = null, name = "Habr"),
            author = "ivan_dev",
            title = "Почему Kotlin Multiplatform — это круто",
            description = "Разбор преимуществ KMP для современной разработки.",
            url = "https://habr.com/ru/post/123456/",
            urlToImage = null,
            publishedAt = "2026-03-03T15:30:00Z",
            content = "Kotlin Multiplatform позволяет переиспользовать бизнес-логику между Android, iOS, Desktop и Web. Это значительно сокращает время разработки и количество ошибок.",
            isPublished = true
        ),
        Article(
            source = Source(id = "bbc-news", name = "BBC News"),
            author = null,
            title = "Global Economic Outlook 2026",
            description = "Analysts predict a stable growth for the upcoming year.",
            url = "https://www.bbc.com/news/business-654321",
            urlToImage = null,
            publishedAt = "2026-03-02T08:00:00Z",
            content = "The world economy is showing signs of recovery and stability after a turbulent few years. Energy prices are stabilizing, and inflation rates are dropping in many major economies.",
            isPublished = true
        ),
        Article(
            source = Source(id = null, name = "Draft Source"),
            author = "Author X",
            title = "Черновик секретной статьи",
            description = "Вы не должны это видеть",
            url = "https://example.com/draft",
            urlToImage = null,
            publishedAt = "2026-03-01T12:00:00Z",
            content = "Это контент черновика, который должен быть скрыт от всех пользователей.",
            isPublished = false
        )
    )
    
    override suspend fun getAll(): Result<List<Article>> {
        return Result.Success(articles)
    }
    
    override suspend fun getPublished(): Result<List<Article>> {
        return Result.Success(articles.filter { it.isPublished })
    }
    
    override suspend fun searchByTitle(query: String): Result<List<Article>> {
        val filtered = articles.filter {
            it.title.contains(query, ignoreCase = true) 
        }
        return Result.Success(filtered)
    }
}