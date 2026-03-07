package com.example.newsapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val error by viewModel.error
    val currentUser by viewModel.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegisterMode) "Регистрация" else "Авторизация",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Lock,
                        contentDescription = "Показать/скрыть пароль"
                    )
                }
            },
            isError = error != null,
            singleLine = true
        )
        
        if (error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { 
                if (isRegisterMode) {
                    viewModel.register(username, password)
                } else {
                    viewModel.login(username, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegisterMode) "Создать аккаунт" else "Войти")
        }
        
        TextButton(onClick = { 
            isRegisterMode = !isRegisterMode 
            viewModel.clearError()
        }) {
            Text(if (isRegisterMode) "Уже есть аккаунт? Войти" else "Нет аккаунта? Зарегистрироваться")
        }

        TextButton(onClick = onLoginSuccess) {
            Text("Продолжить без входа")
        }
    }
}