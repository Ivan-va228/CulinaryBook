
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
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.IOException
import retrofit2.HttpException
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

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var mealIds = emptyList<String>()
    private var nextIndex = 0
    private var requestNumber = 0
    private var loadedCategory: String? = null

    private fun getErrorMessage(e: Exception): String {
        return when (e) {
            is IOException -> "No internet connection. Check your network."
            is HttpException -> "Server error. Please try again later."
            is SerializationException -> "Invalid server response. Please try again."
            else -> "Something went wrong. Please try again."
        }
    }

    fun retry(category: String) {
        if (categories.isEmpty()) {
            loadCategories()
        } else {
            loadRecipes(category, force = true)
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            errorMessage = null

            try {
                val response = MealApiService.api.getCategories()
                val result = response.categories.orEmpty()
                    .mapNotNull { it.name }

                if (result.isEmpty()) {
                    throw SerializationException("Invalid categories response")
                }

                categories = result

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMessage = getErrorMessage(e)
            }
        }
    }

    fun loadRecipes(category: String, force: Boolean = false) {

        if (!force && loadedCategory == category && errorMessage == null) {
            return
        }

        val currentRequest = ++requestNumber

        viewModelScope.launch {
            isLoading = true
            isLoadingMore = false
            errorMessage = null
            recipes = emptyList()
            mealIds = emptyList()
            nextIndex = 0
            hasMore = true

            try {
                val response =
                    MealApiService.api.getMealsByCategory(category)

                val meals = response.meals.orEmpty()

                if (meals.any { it.id.isNullOrBlank() }) {
                    throw SerializationException("Invalid meal data")
                }

                mealIds = meals.map { it.id!! }
                loadedCategory = category

                if (mealIds.isNotEmpty()) {
                    loadNextBatch(currentRequest)
                } else {
                    hasMore = false
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (currentRequest == requestNumber) {
                    errorMessage = getErrorMessage(e)
                    recipes = emptyList()
                    loadedCategory = null
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
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (currentRequest == requestNumber) {
                    errorMessage = getErrorMessage(e)
                }
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
                    val response = MealApiService.api.getMealById(id)

                    val meal = response.meals?.firstOrNull()
                        ?: throw SerializationException("Invalid meal response")

                    meal.toRecipe()
                }
            }.awaitAll()
        }

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
                    ?: throw SerializationException("Invalid meal response")

                onResult(meal.toRecipe())

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMessage = getErrorMessage(e)
                onResult(null)
            }
        }
    }
}
