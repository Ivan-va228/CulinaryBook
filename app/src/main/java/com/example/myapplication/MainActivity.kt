
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsDataStore = remember {
                SettingsDataStore(applicationContext)
            }

            val sortByTime by settingsDataStore.sortByTime
                .collectAsState(initial = false)

            val darkTheme by settingsDataStore.darkTheme
                .collectAsState(initial = false)

            val coroutineScope = rememberCoroutineScope()

            MyApplicationTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            sortByTime = sortByTime,
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
                            sortByTime = sortByTime,
                            onSortByTimeChange = { value ->
                                coroutineScope.launch {
                                    settingsDataStore.saveSortByTime(value)
                                }
                            },
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
                        val recipeId =
                            backStackEntry.arguments?.getInt("recipeId")

                        val recipe = recipes.find {
                            it.id == recipeId
                        }

                        RecipeDetailsScreen(
                            recipe = recipe,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (Int) -> Unit,
    sortByTime: Boolean,
    onSettingsClick: () -> Unit,
    viewModel: MainScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val category = state.category

    val categories = listOf(
        "Усі",
        "Піца",
        "Десерт",
        "Сніданок"
    )

    val filteredRecipes = if (category == "Усі") {
        recipes
    } else {
        recipes.filter { it.category == category }
    }

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
                title = { Text("Кулінарна книга") },
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

@Composable
fun CategoryRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        categories.forEach { category ->
            Button(
                onClick = {
                    onCategorySelected(category)
                }
            ) {
                Text(category)
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(12.dp)) {
            Box {
                Image(
                    painter = painterResource(recipe.image),
                    contentDescription = recipe.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Text(
                    recipe.category,
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                recipe.name,
                fontSize = 22.sp
            )

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Складність: ${recipe.difficulty}")
                Text("Час: ${recipe.time}")
            }
        }
    }
}
