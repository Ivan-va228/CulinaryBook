
package com.example.myapplication.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipe: Recipe?,
    isLoading: Boolean = false,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(recipe?.name ?: "Recipe")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text(
                            text = "⬅",
                            fontSize = 36.sp
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            recipe == null -> {
                Text(
                    text = "Failed to load recipe",
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = recipe.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        recipe.name,
                        fontSize = 26.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("Category: ${recipe.category}")
                    Text("Cuisine: ${recipe.area}")

                    if (recipe.tags.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text("Tags: ${recipe.tags}")
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Ingredients",
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        recipe.ingredients.ifBlank {
                            "No ingredients available"
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Instructions",
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        recipe.instructions.ifBlank {
                            "No instructions available"
                        }
                    )

                    if (recipe.youtube.isNotBlank()) {
                        Spacer(Modifier.height(16.dp))
                        Text("Video: ${recipe.youtube}")
                    }

                    if (recipe.source.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text("Source: ${recipe.source}")
                    }
                }
            }
        }
    }
}
