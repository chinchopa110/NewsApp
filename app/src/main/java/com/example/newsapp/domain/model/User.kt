package com.example.newsapp.domain.model

import java.util.Date

data class User(
    val id: String,
    val username: String,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val registeredAt: Date,
    val isVerified: Boolean = false
)