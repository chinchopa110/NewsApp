package com.example.newsapp.data.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SduiApiService {
    @GET("server/echo/{echoPath}")
    suspend fun getScreen(
        @Path(value = "echoPath", encoded = true) echoPath: String
    ): Response<JsonObject>

    @POST("server/echo/{echoPath}")
    suspend fun uploadScreen(
        @Path(value = "echoPath", encoded = true) echoPath: String,
        @Body body: JsonObject
    ): Response<JsonObject>
}
