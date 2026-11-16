
package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())

    val state: StateFlow<MainScreenState> = _state

    fun selectCategory(category: String) {
        _state.value = _state.value.copy(
            category = category,
            error = null
        )
    }

    fun showError(message: String) {
        _state.value = _state.value.copy(
            error = message
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }
}
