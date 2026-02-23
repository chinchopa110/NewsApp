package com.example.newsapp.domain.service

import com.example.newsapp.domain.model.*
import com.example.newsapp.domain.repository.UserRepository

class AuthService(
    private val userRepository: UserRepository
) : BaseService() {
    
    private var currentUser: User? = null
    
    override fun getServiceName(): String = "AuthService"
    
    suspend fun login(username: String, password: String): Result<User> {
        logOperation("Попытка входа: $username")
        
        if (!validateNotEmpty(username, "username") || !validateNotEmpty(password, "password")) {
            return Result.Error("Имя пользователя и пароль обязательны")
        }
        
        val result = userRepository.getByUsername(username)
        
        if (result is Result.Success) {
            currentUser = result.data
        }
        
        return result
    }
    
    fun isAuthenticated(): Boolean {
        return currentUser != null
    }
    
    fun getCurrentUser(): User? {
        return currentUser
    }
    
    fun logout() {
        logOperation("Выход пользователя")
        currentUser = null
    }
}