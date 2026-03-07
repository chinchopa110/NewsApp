package com.example.newsapp.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            when (val result = userRepository.getByUsername(username)) {
                is Result.Success -> {
                    if (result.data.password == password) {
                        _currentUser.value = result.data
                        _error.value = null
                    } else {
                        _error.value = "Неверный пароль"
                    }
                }
                is Result.Error -> {
                    _error.value = "Пользователь не найден"
                }
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val newUser = User(
                id = "", // Will be set by repository
                username = username,
                password = password
            )
            when (val result = userRepository.register(newUser)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    _error.value = null
                }
                is Result.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }

    fun blockAuthor(author: String) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(blockedAuthors = user.blockedAuthors + author)
        updateUser(updatedUser)
    }

    fun blockSourceId(sourceId: String) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(blockedSourceIds = user.blockedSourceIds + sourceId)
        updateUser(updatedUser)
    }

    fun blockSourceName(sourceName: String) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(blockedSourceNames = user.blockedSourceNames + sourceName)
        updateUser(updatedUser)
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            when (val result = userRepository.update(user)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {
                    _error.value = result.message
                }
            }
        }
    }
}