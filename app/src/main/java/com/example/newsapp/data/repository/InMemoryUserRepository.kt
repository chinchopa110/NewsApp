package com.example.newsapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryUserRepository private constructor(context: Context) : UserRepository {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    
    private val _user = MutableStateFlow(loadUser())
    override val user: StateFlow<User> = _user.asStateFlow()
    
    private fun loadUser(): User {
        val authors = prefs.getStringSet("blocked_authors", emptySet()) ?: emptySet()
        val ids = prefs.getStringSet("blocked_source_ids", emptySet()) ?: emptySet()
        val names = prefs.getStringSet("blocked_source_names", emptySet()) ?: emptySet()
        return User(authors, ids, names)
    }

    override suspend fun updateBlacklist(
        blockedAuthors: Set<String>?,
        blockedSourceIds: Set<String>?,
        blockedSourceNames: Set<String>?
    ) {
        _user.update { currentUser ->
            val newUser = currentUser.copy(
                blockedAuthors = blockedAuthors ?: currentUser.blockedAuthors,
                blockedSourceIds = blockedSourceIds ?: currentUser.blockedSourceIds,
                blockedSourceNames = blockedSourceNames ?: currentUser.blockedSourceNames
            )
            saveUser(newUser)
            newUser
        }
    }

    private fun saveUser(user: User) {
        prefs.edit().apply {
            putStringSet("blocked_authors", user.blockedAuthors)
            putStringSet("blocked_source_ids", user.blockedSourceIds)
            putStringSet("blocked_source_names", user.blockedSourceNames)
            apply()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: InMemoryUserRepository? = null

        fun getInstance(context: Context): InMemoryUserRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: InMemoryUserRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}