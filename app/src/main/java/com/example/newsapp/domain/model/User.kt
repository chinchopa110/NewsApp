package com.example.newsapp.domain.model

data class User(
    val id: String,
    val username: String,
    val password: String,
    val blockedAuthors: Set<String> = emptySet(),
    val blockedSourceIds: Set<String> = emptySet(),
    val blockedSourceNames: Set<String> = emptySet()
)