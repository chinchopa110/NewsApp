package com.example.newsapp.domain.model

import java.util.Date


data class Article(
    val id: String,
    val title: String,
    val content: String,
    val summary: String,
    val author: Author,
    val category: Category,
    val tags: List<String>,
    val imageUrl: String?,
    val publishedAt: Date,
    val updatedAt: Date?,
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val isPublished: Boolean = false,
    val isPremium: Boolean = false
)


data class Author(
    val id: String,
    val name: String,
    val bio: String?,
    val avatarUrl: String?
)


enum class Category(val displayName: String) {
    TECHNOLOGY("Технологии"),
    POLITICS("Политика"),
    SPORTS("Спорт"),
    ENTERTAINMENT("Развлечения"),
    BUSINESS("Бизнес"),
    SCIENCE("Наука"),
    HEALTH("Здоровье"),
    WORLD("Мир");

    companion object {
        fun fromDisplayName(name: String): Category? {
            return values().find { it.displayName == name }
        }
    }
}