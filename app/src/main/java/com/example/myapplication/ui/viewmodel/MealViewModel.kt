
package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.mapper.toRecipe
import com.example.myapplication.data.remote.MealApiService

class MealViewModel : ViewModel() {

    var categories by mutableStateOf<List<String>>(emptyList())
        private set

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isLoadingMore by mutableStateOf(false)
        private set

    var hasMore by mutableStateOf(true)
        private set

    private var mealIds = emptyList<String>()
    private var nextIndex = 0
    private var requestNumber = 0

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = MealApiService.api.getCategories()

                categories = response.categories.orEmpty()
                    .mapNotNull { it.name }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadRecipes(category: String) {
        val currentRequest = ++requestNumber

        viewModelScope.launch {
            isLoading = true
            isLoadingMore = false
            recipes = emptyList()
            mealIds = emptyList()
            nextIndex = 0
            hasMore = true

            try {
                val response =
                    MealApiService.api.getMealsByCategory(category)

                mealIds = response.meals.orEmpty()
                    .mapNotNull { it.id }

                loadNextBatch(currentRequest)

            } catch (e: Exception) {
                e.printStackTrace()
                if (currentRequest == requestNumber) {
                    recipes = emptyList()
                }
            } finally {
                if (currentRequest == requestNumber) {
                    isLoading = false
                }
            }
        }
    }

    fun loadNextPage() {
        if (isLoading || isLoadingMore || !hasMore) return

        val currentRequest = requestNumber

        viewModelScope.launch {
            isLoadingMore = true

            try {
                loadNextBatch(currentRequest)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (currentRequest == requestNumber) {
                    isLoadingMore = false
                }
            }
        }
    }

    private suspend fun loadNextBatch(currentRequest: Int) {
        val batchIds = mealIds
            .drop(nextIndex)
            .take(10)

        if (batchIds.isEmpty()) {
            hasMore = false
            return
        }

        val newRecipes = coroutineScope {
            batchIds.map { id ->
                async {
                    try {
                        MealApiService.api.getMealById(id)
                            .meals
                            ?.firstOrNull()
                            ?.toRecipe()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }.awaitAll()
        }.filterNotNull()

        if (currentRequest == requestNumber) {
            recipes = recipes + newRecipes
            nextIndex += batchIds.size
            hasMore = nextIndex < mealIds.size
        }
    }

    fun loadRecipeDetails(
        id: Int,
        onResult: (Recipe?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    MealApiService.api.getMealById(id.toString())

                val meal = response.meals?.firstOrNull()

                onResult(meal?.toRecipe())

            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}
