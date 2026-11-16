package com.example.myapplication.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.components.CategoryRow
import com.example.myapplication.ui.components.RecipeCard
import com.example.myapplication.ui.viewmodel.MealViewModel
import com.example.myapplication.ui.viewmodel.MainScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (Int) -> Unit,
    sortByTime: Boolean,
    onSettingsClick: () -> Unit,
    mealViewModel: MealViewModel,
    viewModel: MainScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val category = state.category

    LaunchedEffect(category) {
        mealViewModel.loadRecipes(category)
    }

    val categories = listOf(
        "Усі",
        "Піца",
        "Десерт",
        "Сніданок"
    )

    val filteredRecipes = mealViewModel.recipes

    val displayedRecipes = if (sortByTime) {
        filteredRecipes.sortedBy {
            it.time.substringBefore(" ").toIntOrNull() ?: 0
        }
    } else {
        filteredRecipes.sortedBy { it.name }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Кулінарна книга")
                },
                actions = {
                    TextButton(onClick = onSettingsClick) {
                        Text("Налаштування")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CategoryRow(
                categories = categories,
                selectedCategory = category,
                onCategorySelected = {
                    viewModel.selectCategory(it)
                }
            )

            if (mealViewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(displayedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            onItemClick(recipe.id)
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}