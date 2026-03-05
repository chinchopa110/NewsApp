package com.example.newsapp.ui

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.newsapp.domain.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel,
    onLogout: () -> Unit
) {
    val articles by newsViewModel.articles
    val isLoading by newsViewModel.isLoading
    val currentUser by authViewModel.currentUser
    val isDarkTheme by themeViewModel.isDarkTheme

    var selectedArticleForBlock by remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(currentUser) {
        newsViewModel.loadArticles(currentUser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NewsApp") },
                actions = {
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Сменить тему"
                        )
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(articles, key = { it.url }) { article ->
                        ArticleItem(
                            article = article,
                            onLongClick = {
                                if (currentUser != null) {
                                    selectedArticleForBlock = article
                                }
                            }
                        )
                    }
                }
            }
        }

        // Диалог блокировки
        selectedArticleForBlock?.let { article ->
            AlertDialog(
                onDismissRequest = { selectedArticleForBlock = null },
                confirmButton = {
                    TextButton(onClick = { selectedArticleForBlock = null }) {
                        Text("Отмена")
                    }
                },
                title = { Text("Заблокировать контент?") },
                text = {
                    // Весь контент теперь здесь, что дает контроль над отступами
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Компактное расстояние между кнопками
                    ) {
                        Text("Выберите, что именно вы хотите скрыть из ленты:", modifier = Modifier.padding(bottom = 8.dp))

                        article.author?.let { author ->
                            Button(
                                onClick = {
                                    authViewModel.blockAuthor(author)
                                    selectedArticleForBlock = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text("Автор: $author")
                            }
                        }

                        article.source.id?.let { sourceId ->
                            Button(
                                onClick = {
                                    authViewModel.blockSourceId(sourceId)
                                    selectedArticleForBlock = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text("Источник по ID: $sourceId")
                            }
                        }

                        Button(
                            onClick = {
                                authViewModel.blockSourceName(article.source.name)
                                selectedArticleForBlock = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text("СМИ: ${article.source.name}")
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleItem(
    article: Article,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                article.author?.let {
                    Text(text = " • ", style = MaterialTheme.typography.labelMedium)
                    Text(text = it, style = MaterialTheme.typography.labelMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                    context.startActivity(intent)
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Читать оригинал на ${article.source.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                )
            }
            
            Text(
                text = article.publishedAt,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = Color.Gray
            )
        }
    }
}