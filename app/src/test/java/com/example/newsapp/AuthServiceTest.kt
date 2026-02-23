package com.example.newsapp

import com.example.newsapp.data.repository.InMemoryUserRepository
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.service.AuthService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthServiceTest {
    
    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var authService: AuthService
    
    @Before
    fun setup() {
        userRepository = InMemoryUserRepository()
        authService = AuthService(userRepository)
    }
    
    @Test
    fun `login with valid username should return success`() = runBlocking {
        val username = "john_doe"
        val password = "password123"
        
        val result = authService.login(username, password)
        
        assertTrue("Login should succeed with valid username", result is Result.Success)
        assertTrue("User should be authenticated", authService.isAuthenticated())
        
        val user = (result as Result.Success).data
        assertEquals("Username should match", username, user.username)
        assertEquals("User ID should be 1", "1", user.id)
        assertEquals("Email should match", "john@example.com", user.email)
        assertTrue("User should be verified", user.isVerified)
        
        val currentUser = authService.getCurrentUser()
        assertNotNull("Current user should not be null", currentUser)
        assertEquals("Current user should match logged in user", username, currentUser?.username)
    }
    
    @Test
    fun `login with invalid username should return error`() = runBlocking {
        val username = "nonexistent_user"
        val password = "password123"
        
        val result = authService.login(username, password)
        
        assertTrue("Login should fail with invalid username", result is Result.Error)
        assertFalse("User should not be authenticated", authService.isAuthenticated())
        
        val errorMessage = (result as Result.Error).message
        assertTrue(
            "Error message should mention user not found",
            errorMessage.contains("не найден", ignoreCase = true)
        )
        
        val currentUser = authService.getCurrentUser()
        assertNull("Current user should be null after failed login", currentUser)
    }
    
    @Test
    fun `login with empty username should return error`() = runBlocking {
        val username = ""
        val password = "password123"
        
        val result = authService.login(username, password)
        
        assertTrue("Login should fail with empty username", result is Result.Error)
        assertFalse("User should not be authenticated", authService.isAuthenticated())
        
        val errorMessage = (result as Result.Error).message
        assertTrue(
            "Error message should mention required fields",
            errorMessage.contains("обязательны", ignoreCase = true)
        )
    }
    
    @Test
    fun `login with empty password should return error`() = runBlocking {
        val username = "john_doe"
        val password = ""
        
        val result = authService.login(username, password)
        
        assertTrue("Login should fail with empty password", result is Result.Error)
        assertFalse("User should not be authenticated", authService.isAuthenticated())
    }
    
    @Test
    fun `logout should clear current user`() = runBlocking {
        authService.login("john_doe", "password123")
        assertTrue("User should be authenticated before logout", authService.isAuthenticated())
        
        authService.logout()
        
        assertFalse("User should not be authenticated after logout", authService.isAuthenticated())
        assertNull("Current user should be null after logout", authService.getCurrentUser())
    }
    
    @Test
    fun `multiple login attempts should update current user`() = runBlocking {
        val firstLogin = authService.login("john_doe", "password123")
        assertTrue("First login should succeed", firstLogin is Result.Success)
        assertEquals("Current user should be john_doe", "john_doe", authService.getCurrentUser()?.username)
        
        val secondLogin = authService.login("jane_smith", "password456")
        
        assertTrue("Second login should succeed", secondLogin is Result.Success)
        assertEquals("Current user should be jane_smith", "jane_smith", authService.getCurrentUser()?.username)
        assertEquals("User ID should be 2", "2", authService.getCurrentUser()?.id)
    }
}