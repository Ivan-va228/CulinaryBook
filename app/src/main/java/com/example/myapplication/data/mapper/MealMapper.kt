package com.example.myapplication.data.mapper

import com.example.myapplication.R
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.model.MealResponse

fun MealResponse.toRecipe(): Recipe {
    val ingredients = listOf(
        ingredient1 to measure1,
        ingredient2 to measure2,
        ingredient3 to measure3,
        ingredient4 to measure4,
        ingredient5 to measure5,
        ingredient6 to measure6,
        ingredient7 to measure7,
        ingredient8 to measure8,
        ingredient9 to measure9,
        ingredient10 to measure10,
        ingredient11 to measure11,
        ingredient12 to measure12,
        ingredient13 to measure13,
        ingredient14 to measure14,
        ingredient15 to measure15,
        ingredient16 to measure16,
        ingredient17 to measure17,
        ingredient18 to measure18,
        ingredient19 to measure19,
        ingredient20 to measure20
    ).mapNotNull { (ingredient, measure) ->
        ingredient
            ?.takeIf { it.isNotBlank() }
            ?.let { name ->
                val amount = measure?.trim().orEmpty()
                if (amount.isBlank()) name else "$name — $amount"
            }
    }.joinToString("\n")

    return Recipe(
        id = id?.toIntOrNull() ?: 0,
        name = name.orEmpty(),
        image = R.drawable.pizza_salami,
        category = category.orEmpty(),
        difficulty = "Не вказано",
        time = "Не вказано",
        ingredients = ingredients,
        instructions = instructions.orEmpty()
    )
}