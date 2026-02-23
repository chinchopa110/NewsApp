package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Comment
import com.example.newsapp.domain.model.Result

interface CommentRepository : Repository<Comment, String> {
    suspend fun getByArticleId(articleId: String): Result<List<Comment>>
    suspend fun getByUserId(userId: String): Result<List<Comment>>
    suspend fun getReplies(parentCommentId: String): Result<List<Comment>>
}