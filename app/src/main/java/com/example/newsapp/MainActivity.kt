package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.newsapp.data.repository.InMemoryArticleRepository
import com.example.newsapp.data.repository.InMemoryUserRepository
import com.example.newsapp.ui.*
import com.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    
    private val userRepository = InMemoryUserRepository()
    private val articleRepository = InMemoryArticleRepository()
    
    private val themeViewModel = ThemeViewModel()
    private val authViewModel = AuthViewModel(userRepository)
    private val newsViewModel = NewsViewModel(articleRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme
            
            NewsAppTheme(darkTheme = isDarkTheme) {
                var currentScreen by remember { mutableStateOf("login") }
                
                when (currentScreen) {
                    "login" -> LoginScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = {
                            currentScreen = "news"
                        }
                    )
                    "news" -> NewsScreen(
                        newsViewModel = newsViewModel,
                        authViewModel = authViewModel,
                        themeViewModel = themeViewModel,
                        onLogout = {
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }
}