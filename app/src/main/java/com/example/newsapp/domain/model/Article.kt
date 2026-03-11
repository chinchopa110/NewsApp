package com.example.newsapp.domain.model

data class Source(
    val id: String?,
    val name: String
)

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
)
