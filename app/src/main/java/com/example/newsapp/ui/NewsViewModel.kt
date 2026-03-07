package com.example.newsapp.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.User
import com.example.newsapp.domain.repository.ArticleRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _articles = mutableStateOf<List<Article>>(emptyList())
    val articles: State<List<Article>> = _articles

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadArticles(currentUser: User?) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val result = articleRepository.getPublished()

            when (result) {
                is Result.Success -> {
                    var filteredList = result.data
                    if (currentUser != null) {
                        filteredList = filteredList.filter { article ->
                            val authorBlocked = article.author != null && currentUser.blockedAuthors.contains(article.author)
                            val sourceIdBlocked = article.source.id != null && currentUser.blockedSourceIds.contains(article.source.id)
                            val sourceNameBlocked = currentUser.blockedSourceNames.contains(article.source.name)
                            
                            !authorBlocked && !sourceIdBlocked && !sourceNameBlocked
                        }
                    }
                    _articles.value = filteredList
                }
                is Result.Error -> _articles.value = emptyList()
            }
            _isLoading.value = false
        }
    }
}