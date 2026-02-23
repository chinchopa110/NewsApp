package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Result

interface Repository<T, ID> {
    suspend fun getById(id: ID): Result<T>
    suspend fun getAll(): Result<List<T>>
    suspend fun save(entity: T): Result<T>
    suspend fun delete(id: ID): Result<Boolean>
    suspend fun update(entity: T): Result<T>
}