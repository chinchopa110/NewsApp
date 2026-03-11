package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BlacklistViewModel(private val userRepository: UserRepository) : ViewModel() {
    
    val user: StateFlow<User> = userRepository.user

    fun unblockAuthor(author: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedAuthors
            userRepository.updateBlacklist(blockedAuthors = currentBlocked - author)
        }
    }

    fun unblockSourceId(sourceId: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedSourceIds
            userRepository.updateBlacklist(blockedSourceIds = currentBlocked - sourceId)
        }
    }

    fun unblockSourceName(sourceName: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedSourceNames
            userRepository.updateBlacklist(blockedSourceNames = currentBlocked - sourceName)
        }
    }

    fun blockAuthor(author: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedAuthors
            userRepository.updateBlacklist(blockedAuthors = currentBlocked + author)
        }
    }

    fun blockSourceId(sourceId: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedSourceIds
            userRepository.updateBlacklist(blockedSourceIds = currentBlocked + sourceId)
        }
    }

    fun blockSourceName(sourceName: String) {
        viewModelScope.launch {
            val currentBlocked = user.value.blockedSourceNames
            userRepository.updateBlacklist(blockedSourceNames = currentBlocked + sourceName)
        }
    }
}