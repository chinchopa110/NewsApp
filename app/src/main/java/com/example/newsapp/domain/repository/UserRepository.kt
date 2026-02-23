package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User

interface UserRepository : Repository<User, String> {
    suspend fun getByUsername(username: String): Result<User>
    suspend fun getByEmail(email: String): Result<User>
    suspend fun verifyUser(userId: String): Result<Boolean>
}