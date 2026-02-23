package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.UserRepository
import java.util.Date

class InMemoryUserRepository : UserRepository {
    
    private val users = mutableMapOf<String, User>()
    
    init {
        val testUsers = listOf(
            User(
                id = "1",
                username = "john_doe",
                email = "john@example.com",
                fullName = "John Doe",
                avatarUrl = "https://example.com/avatars/john.jpg",
                registeredAt = Date(),
                isVerified = true
            ),
            User(
                id = "2",
                username = "jane_smith",
                email = "jane@example.com",
                fullName = "Jane Smith",
                avatarUrl = "https://example.com/avatars/jane.jpg",
                registeredAt = Date(),
                isVerified = true
            ),
            User(
                id = "3",
                username = "test_user",
                email = "test@example.com",
                fullName = "Test User",
                avatarUrl = null,
                registeredAt = Date(),
                isVerified = false
            )
        )
        
        testUsers.forEach { users[it.id] = it }
    }
    
    override suspend fun getById(id: String): Result<User> {
        return users[id]?.let { Result.Success(it) }
            ?: Result.Error("Пользователь с ID $id не найден")
    }
    
    override suspend fun getAll(): Result<List<User>> {
        return Result.Success(users.values.toList())
    }
    
    override suspend fun save(entity: User): Result<User> {
        users[entity.id] = entity
        return Result.Success(entity)
    }
    
    override suspend fun delete(id: String): Result<Boolean> {
        return if (users.remove(id) != null) {
            Result.Success(true)
        } else {
            Result.Error("Пользователь с ID $id не найден")
        }
    }
    
    override suspend fun update(entity: User): Result<User> {
        return if (users.containsKey(entity.id)) {
            users[entity.id] = entity
            Result.Success(entity)
        } else {
            Result.Error("Пользователь с ID ${entity.id} не найден")
        }
    }
    
    override suspend fun getByUsername(username: String): Result<User> {
        val user = users.values.find { it.username == username }
        return user?.let { Result.Success(it) }
            ?: Result.Error("Пользователь с именем $username не найден")
    }
    
    override suspend fun getByEmail(email: String): Result<User> {
        val user = users.values.find { it.email == email }
        return user?.let { Result.Success(it) }
            ?: Result.Error("Пользователь с email $email не найден")
    }
    
    override suspend fun verifyUser(userId: String): Result<Boolean> {
        val user = users[userId]
        return if (user != null) {
            users[userId] = user.copy(isVerified = true)
            Result.Success(true)
        } else {
            Result.Error("Пользователь с ID $userId не найден")
        }
    }
}