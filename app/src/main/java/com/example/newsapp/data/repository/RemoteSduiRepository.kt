package com.example.newsapp.data.repository

import com.example.newsapp.data.network.RetrofitClient
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.SduiScreen
import com.example.newsapp.domain.repository.SduiRepository
import com.example.newsapp.ui.sdui.SduiJsonMapper
import java.net.URLEncoder

class RemoteSduiRepository : SduiRepository {

    override suspend fun getScreen(path: String): Result<SduiScreen> {
        return try {
            val response = RetrofitClient.sduiApi.getScreen(path.encodeEchoPath())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(SduiJsonMapper.fromJson(body))
                } else {
                    Result.Error("SDUI response body is empty")
                }
            } else {
                Result.Error("SDUI API error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("SDUI network error: ${e.message}")
        }
    }

    override suspend fun uploadScreen(path: String, screen: SduiScreen): Result<Unit> {
        return try {
            val response = RetrofitClient.sduiApi.uploadScreen(
                path.encodeEchoPath(),
                SduiJsonMapper.toJson(screen)
            )
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("SDUI upload error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("SDUI upload network error: ${e.message}")
        }
    }

    private fun String.encodeEchoPath(): String {
        return URLEncoder.encode(this, Charsets.UTF_8.name())
            .replace("+", "%20")
    }
}
