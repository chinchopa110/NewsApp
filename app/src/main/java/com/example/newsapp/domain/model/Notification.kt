package com.example.newsapp.domain.model

import java.util.Date

data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val createdAt: Date,
    val isRead: Boolean = false,
    val relatedEntityId: String? = null
)

sealed class NotificationType {
    object NewArticle : NotificationType()
    object CommentReply : NotificationType()
    object ArticleLike : NotificationType()
    object SystemMessage : NotificationType()
    data class Custom(val typeName: String) : NotificationType()
}