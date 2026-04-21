package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.SduiScreen

interface SduiRepository {
    suspend fun getScreen(path: String): Result<SduiScreen>
    suspend fun uploadScreen(path: String, screen: SduiScreen): Result<Unit>
}
