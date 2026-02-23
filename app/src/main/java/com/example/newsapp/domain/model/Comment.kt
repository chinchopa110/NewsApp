package com.example.newsapp.domain.model

import java.util.Date

data class Comment(
    val id: String,
    val articleId: String,
    val userId: String,
    val userName: String,
    val content: String,
    val createdAt: Date,
    val updatedAt: Date?,
    val likeCount: Int = 0,
    val parentCommentId: String? = null,
    val isEdited: Boolean = false
)