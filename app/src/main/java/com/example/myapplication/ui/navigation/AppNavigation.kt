package com.example.myapplication.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.data.preferences.SettingsDataStore
import com.example.myapplication.ui.screens.main.MainScreen
import com.example.myapplication.ui.screens.details.RecipeDetailsScreen
import com.example.myapplication.ui.screens.settings.SettingsScreen
import com.example.myapplication.ui.viewmodel.MealViewModel

@Composable
fun AppNavigation() {

    val context = LocalContext.current

    val settingsDataStore = remember {
        SettingsDataStore(context)
    }

    val darkTheme by settingsDataStore.darkTheme
        .collectAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()

    MyApplicationTheme(darkTheme = darkTheme) {

        val mealViewModel: MealViewModel = viewModel()
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainScreen(
                    mealViewModel = mealViewModel,
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onItemClick = { recipeId ->
                        navController.navigate("details/$recipeId")
                    }
                )
            }

            composable("settings") {
                SettingsScreen(
                    darkTheme = darkTheme,
                    onDarkThemeChange = { value ->
                        coroutineScope.launch {
                            settingsDataStore.saveDarkTheme(value)
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "details/{recipeId}",
                arguments = listOf(
                    navArgument("recipeId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val recipeId = backStackEntry.arguments
                    ?.getInt("recipeId")

                var recipe by remember(recipeId) {
                    mutableStateOf(
                        mealViewModel.recipes.find {
                            it.id == recipeId
                        }
                    )
                }

                var isLoadingDetails by remember(recipeId) {
                    mutableStateOf(true)
                }

                LaunchedEffect(recipeId) {
                    if (recipeId != null) {
                        mealViewModel.loadRecipeDetails(recipeId) { loadedRecipe ->
                            recipe = loadedRecipe
                            isLoadingDetails = false
                        }
                    } else {
                        isLoadingDetails = false
                    }
                }

                RecipeDetailsScreen(
                    recipe = recipe,
                    isLoading = isLoadingDetails,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}