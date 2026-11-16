package com.example.myapplication.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Text(recipe?.name ?: "Рецепт")
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
                    text = "Не вдалося завантажити рецепт",
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
                    Image(
                        painter = painterResource(recipe.image),
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

                    Text("Категорія: ${recipe.category}")
                    Text("Складність: ${recipe.difficulty}")
                    Text("Час приготування: ${recipe.time}")

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Інгредієнти",
                        fontSize = 20.sp
                    )

                    Text(
                        text = recipe.ingredients.ifBlank {
                            "Інгредієнти відсутні"
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Спосіб приготування",
                        fontSize = 20.sp
                    )

                    Text(
                        text = recipe.instructions.ifBlank {
                            "Інструкція відсутня"
                        }
                    )
                }
            }
        }
    }
}