package com.example.myapplication

data class Recipe(
    val id: Int,
    val name: String,
    val image: Int,
    val category: String,
    val difficulty: String,
    val time: String
)

val recipes = listOf(
    Recipe(
        id = 1,
        name = "Піца Салямі",
        image = R.drawable.pizza,
        category = "Піца",
        difficulty = "Легко",
        time = "30 хв"
    ),
    Recipe(
        id = 2,
        name = "Млинці",
        image = R.drawable.pancakes,
        category = "Сніданки",
        difficulty = "Легко",
        time = "25 хв"
    ),
    Recipe(
        id = 3,
        name = "Чізкейк",
        image = R.drawable.cheesecake,
        category = "Десерт",
        difficulty = "Важко",
        time = "60 хв"
    )
)