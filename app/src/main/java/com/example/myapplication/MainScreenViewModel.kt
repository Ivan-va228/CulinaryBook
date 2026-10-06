package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())

    val state: StateFlow<MainScreenState> = _state

    fun selectCategory(category: String) {
        _state.value = _state.value.copy(
            category = category
        )
    }
}