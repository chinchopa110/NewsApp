package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val user: StateFlow<User>
    suspend fun updateBlacklist(
        blockedAuthors: Set<String>? = null,
        blockedSourceIds: Set<String>? = null,
        blockedSourceNames: Set<String>? = null
    )
}