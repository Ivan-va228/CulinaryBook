
package com.example.myapplication.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.snapshotFlow
import com.example.myapplication.ui.components.CategoryRow
import com.example.myapplication.ui.components.RecipeCard
import com.example.myapplication.ui.viewmodel.MealViewModel
import com.example.myapplication.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    mealViewModel: MealViewModel,
    listState: LazyListState,
    viewModel: MainScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val category = state.category
    val searchQuery = state.searchQuery
    val isSearching = searchQuery.isNotBlank()

    val categories = mealViewModel.categories
    val displayedRecipes = mealViewModel.recipes.sortedBy { it.name }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        mealViewModel.loadCategories()
    }

    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && category !in categories) {
            viewModel.selectCategory(categories.first())
        }
    }

    LaunchedEffect(category, categories, searchQuery) {
        if (category in categories) {
            if (searchQuery.isNotBlank()) {
                delay(500)
            }

            mealViewModel.loadRecipes(
                category = category,
                searchQuery = searchQuery,
                force = true
            )
        }
    }

    LaunchedEffect(
        listState,
        displayedRecipes.size,
        mealViewModel.isLoadingMore,
        mealViewModel.hasMore,
        searchQuery
    ) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount

            lastVisibleItem >= totalItems - 4
        }.collect { shouldLoadMore ->
            if (!isSearching &&
                shouldLoadMore &&
                displayedRecipes.isNotEmpty()
            ) {
                mealViewModel.loadNextPage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Recipe Book")
                },
                actions = {
                    TextButton(onClick = onSettingsClick) {
                        Text("Settings")
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = {
                    Text("Search recipes")
                },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                viewModel.clearSearch()
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        ) {
                            Text("Clear")
                        }
                    }
                }
            )

            if (!isSearching) {
                CategoryRow(
                    categories = categories,
                    selectedCategory = category,
                    onCategorySelected = {
                        viewModel.selectCategory(it)

                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                    }
                )
            }

            when {
                mealViewModel.errorMessage != null &&
                        displayedRecipes.isEmpty() -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(mealViewModel.errorMessage!!)

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    mealViewModel.retry(
                                        category,
                                        searchQuery
                                    )
                                }
                            ) {
                                Text("Try again")
                            }
                        }
                    }
                }

                mealViewModel.isLoading && displayedRecipes.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                categories.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                displayedRecipes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                if (isSearching) {
                                    "No recipes found for \"$searchQuery\"."
                                } else {
                                    "No recipes found in this category."
                                }
                            )

                            if (isSearching) {
                                Spacer(Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.clearSearch()
                                    }
                                ) {
                                    Text("Clear search")
                                }
                            }
                        }
                    }
                }

                else -> {
                    val recipeList: @Composable () -> Unit = {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(
                                items = displayedRecipes,
                                key = { recipe -> recipe.id }
                            ) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = {
                                        onItemClick(recipe.id)
                                    }
                                )

                                Spacer(Modifier.height(16.dp))
                            }

                            if (mealViewModel.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }

                    if (isSearching) {
                        recipeList()
                    } else {
                        PullToRefreshBox(
                            isRefreshing = mealViewModel.isRefreshing,
                            onRefresh = {
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                    mealViewModel.refreshRecipes(category)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            recipeList()
                        }
                    }
                }
            }
        }
    }
}
