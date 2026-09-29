package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var category by remember { mutableStateOf("Усі") }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Кулінарна книга") }
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
                onCategorySelected = { category = it }
            )

            if (filteredRecipes.isEmpty()) {

                Text(
                    "Рецептів у цій категорії немає",
                    modifier = Modifier.padding(16.dp)
                )

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    items(filteredRecipes) { recipe ->
                        RecipeCard(recipe)
                        Spacer(Modifier.height(16.dp))
                    }
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
fun RecipeCard(recipe: Recipe) {

    Card(Modifier.fillMaxWidth()) {

        Column(Modifier.padding(12.dp)) {

            Box {
                Image(
                    painterResource(recipe.image),
                    recipe.name,
                    Modifier
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