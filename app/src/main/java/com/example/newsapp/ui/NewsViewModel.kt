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

    private var currentPage = 1
    private var isLastPage = false
    private var isCurrentlyLoadingNextPage = false

    init {
        viewModelScope.launch {
            userRepository.user.collectLatest { user ->
                resetAndLoad(user)
            }
        }
    }

    private fun resetAndLoad(currentUser: User) {
        currentPage = 1
        isLastPage = false
        _articles.value = emptyList()
        loadArticles(currentUser, isRefresh = true)
    }

    private fun loadArticles(currentUser: User, isRefresh: Boolean = false) {
        if (isLastPage || (isCurrentlyLoadingNextPage && !isRefresh)) return

        viewModelScope.launch {
            if (isRefresh) {
                _isLoading.value = true
            } else {
                isCurrentlyLoadingNextPage = true
            }
            
            val result = articleRepository.getTopHeadlines(currentPage)

            when (result) {
                is Result.Success -> {
                    val newArticles = result.data
                    if (newArticles.isEmpty()) {
                        isLastPage = true
                    } else {
                        val filteredList = newArticles.filter { article ->
                            val authorBlocked = article.author != null && currentUser.blockedAuthors.contains(article.author)
                            val sourceIdBlocked = article.source.id != null && currentUser.blockedSourceIds.contains(article.source.id)
                            val sourceNameBlocked = currentUser.blockedSourceNames.contains(article.source.name)
                            
                            !authorBlocked && !sourceIdBlocked && !sourceNameBlocked
                        }
                        
                        if (isRefresh) {
                            _articles.value = filteredList
                        } else {
                            _articles.value = _articles.value + filteredList
                        }
                        currentPage++
                    }
                }
                is Result.Error -> {
                    if (isRefresh) _articles.value = emptyList()
                }
            }
            _isLoading.value = false
            isCurrentlyLoadingNextPage = false
        }
    }
    
    fun loadNextPage() {
        loadArticles(userRepository.user.value)
    }

    fun refresh() {
        resetAndLoad(userRepository.user.value)
    }
}
