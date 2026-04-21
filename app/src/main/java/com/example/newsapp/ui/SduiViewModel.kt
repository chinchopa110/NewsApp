package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Result
import com.example.newsapp.domain.model.SduiScreen
import com.example.newsapp.domain.repository.SduiRepository
import com.example.newsapp.ui.sdui.SduiScreenSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SduiViewModel(
    private val repository: SduiRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<SduiScreenState>(SduiScreenState.Loading)
    val screenState: StateFlow<SduiScreenState> = _screenState.asStateFlow()

    init {
        loadScreen()
    }

    fun loadScreen() {
        viewModelScope.launch {
            _screenState.value = SduiScreenState.Loading

            val uploadResult = repository.uploadScreen(
                path = SduiScreenSeeder.REMOTE_PATH,
                screen = SduiScreenSeeder.createHotNewsScreen()
            )

            if (uploadResult is Result.Error) {
                _screenState.value = SduiScreenState.Error(uploadResult.message)
                return@launch
            }

            when (val result = repository.getScreen(SduiScreenSeeder.REMOTE_PATH)) {
                is Result.Success -> _screenState.value = SduiScreenState.Success(result.data)
                is Result.Error -> _screenState.value = SduiScreenState.Error(result.message)
            }
        }
    }

    class Factory(
        private val repository: SduiRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SduiViewModel(repository) as T
        }
    }
}

sealed interface SduiScreenState {
    data object Loading : SduiScreenState
    data class Success(val screen: SduiScreen) : SduiScreenState
    data class Error(val message: String) : SduiScreenState
}
