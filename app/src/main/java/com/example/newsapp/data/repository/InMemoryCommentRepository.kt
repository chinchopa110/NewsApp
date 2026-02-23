package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.Comment
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.repository.CommentRepository
import java.util.Date

class InMemoryCommentRepository : CommentRepository {
    
    private val comments = mutableMapOf<String, Comment>()
    
    init {
        val testComments = listOf(
            Comment(
                id = "1",
                articleId = "1",
                userId = "1",
                userName = "John Doe",
                content = "Отличная статья! Очень информативно.",
                createdAt = Date(),
                updatedAt = null,
                likeCount = 5,
                parentCommentId = null,
                isEdited = false
            ),
            Comment(
                id = "2",
                articleId = "1",
                userId = "2",
                userName = "Jane Smith",
                content = "Согласна с автором, AI действительно меняет мир.",
                createdAt = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
                updatedAt = null,
                likeCount = 3,
                parentCommentId = null,
                isEdited = false
            ),
            Comment(
                id = "3",
                articleId = "1",
                userId = "3",
                userName = "Test User",
                content = "Спасибо за разъяснение!",
                createdAt = Date(System.currentTimeMillis() - 7200000), // 2 hours ago
                updatedAt = null,
                likeCount = 1,
                parentCommentId = "1",
                isEdited = false
            ),
            Comment(
                id = "4",
                articleId = "2",
                userId = "1",
                userName = "John Doe",
                content = "Невероятный матч! Поздравляю команду!",
                createdAt = Date(),
                updatedAt = null,
                likeCount = 10,
                parentCommentId = null,
                isEdited = false
            ),
            Comment(
                id = "5",
                articleId = "2",
                userId = "2",
                userName = "Jane Smith",
                content = "Лучшая игра сезона!",
                createdAt = Date(System.currentTimeMillis() - 1800000), // 30 min ago
                updatedAt = Date(),
                likeCount = 7,
                parentCommentId = null,
                isEdited = true
            )
        )
        
        testComments.forEach { comments[it.id] = it }
    }
    
    override suspend fun getById(id: String): Result<Comment> {
        return comments[id]?.let { Result.Success(it) }
            ?: Result.Error("Комментарий с ID $id не найден")
    }
    
    override suspend fun getAll(): Result<List<Comment>> {
        return Result.Success(comments.values.toList())
    }
    
    override suspend fun save(entity: Comment): Result<Comment> {
        comments[entity.id] = entity
        return Result.Success(entity)
    }
    
    override suspend fun delete(id: String): Result<Boolean> {
        return if (comments.remove(id) != null) {
            Result.Success(true)
        } else {
            Result.Error("Комментарий с ID $id не найден")
        }
    }
    
    override suspend fun update(entity: Comment): Result<Comment> {
        return if (comments.containsKey(entity.id)) {
            comments[entity.id] = entity
            Result.Success(entity)
        } else {
            Result.Error("Комментарий с ID ${entity.id} не найден")
        }
    }
    
    override suspend fun getByArticleId(articleId: String): Result<List<Comment>> {
        val filtered = comments.values.filter { it.articleId == articleId }
        return Result.Success(filtered)
    }
    
    override suspend fun getByUserId(userId: String): Result<List<Comment>> {
        val filtered = comments.values.filter { it.userId == userId }
        return Result.Success(filtered)
    }
    
    override suspend fun getReplies(parentCommentId: String): Result<List<Comment>> {
        val filtered = comments.values.filter { it.parentCommentId == parentCommentId }
        return Result.Success(filtered)
    }
}