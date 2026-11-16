
package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.mapper.toRecipe
import com.example.myapplication.data.remote.MealApiService

class MealViewModel : ViewModel() {

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var requestNumber = 0

    fun loadRecipes(category: String = "Усі") {
        val currentRequest = ++requestNumber

        viewModelScope.launch {
            isLoading = true

            try {
                val response = when (category) {
                    "Десерт" ->
                        MealApiService.api.getMealsByCategory("Dessert")

                    "Сніданок" ->
                        MealApiService.api.getMealsByCategory("Breakfast")

                    "Піца" ->
                        MealApiService.api.searchMeals("pizza")

                    else ->
                        MealApiService.api.searchMeals("chicken")
                }

                val newRecipes = response.meals.orEmpty()
                    .map { it.toRecipe() }

                if (currentRequest == requestNumber) {
                    recipes = newRecipes
                }

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
