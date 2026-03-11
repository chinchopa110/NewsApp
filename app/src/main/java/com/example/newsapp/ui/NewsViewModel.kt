package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.ArticleRepository
import com.example.newsapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsViewModel(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            userRepository.user.collectLatest { user ->
                loadArticles(user)
            }
        }
    }

    private fun loadArticles(currentUser: User) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val result = articleRepository.getTopHeadlines()

            when (result) {
                is Result.Success -> {
                    val filteredList = result.data.filter { article ->
                        val authorBlocked = article.author != null && currentUser.blockedAuthors.contains(article.author)
                        val sourceIdBlocked = article.source.id != null && currentUser.blockedSourceIds.contains(article.source.id)
                        val sourceNameBlocked = currentUser.blockedSourceNames.contains(article.source.name)
                        
                        !authorBlocked && !sourceIdBlocked && !sourceNameBlocked
                    }
                    _articles.value = filteredList
                }
                is Result.Error -> _articles.value = emptyList()
            }
            _isLoading.value = false
        }
    }
    
    fun refresh() {
        loadArticles(userRepository.user.value)
    }
}