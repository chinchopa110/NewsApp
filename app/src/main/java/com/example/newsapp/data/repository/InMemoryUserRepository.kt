package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.UserRepository
import java.util.UUID

class InMemoryUserRepository : UserRepository {
    
    private val users = mutableMapOf<String, User>()
    
    init {
        val testUsers = listOf(
            User(
                id = UUID.randomUUID().toString(),
                username = "ivan",
                password = "123"
            ),
            User(
                id = UUID.randomUUID().toString(),
                username = "maria",
                password = "321"
            )
        )
        testUsers.forEach { users[it.id] = it }
    }
    
    override suspend fun getById(id: String): Result<User> {
        return users[id]?.let { Result.Success(it) }
            ?: Result.Error("Пользователь не найден")
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
            Result.Error("Пользователь не найден")
        }
    }
    
    override suspend fun update(entity: User): Result<User> {
        return if (users.containsKey(entity.id)) {
            users[entity.id] = entity
            Result.Success(entity)
        } else {
            Result.Error("Пользователь не найден")
        }
    }
    
    override suspend fun getByUsername(username: String): Result<User> {
        val user = users.values.find { it.username == username }
        return user?.let { Result.Success(it) }
            ?: Result.Error("Пользователь $username не найден")
    }
    
    override suspend fun register(user: User): Result<User> {
        if (users.values.any { it.username == user.username }) {
            return Result.Error("Логин уже занят")
        }
        val newUser = user.copy(id = UUID.randomUUID().toString())
        users[newUser.id] = newUser
        return Result.Success(newUser)
    }
}