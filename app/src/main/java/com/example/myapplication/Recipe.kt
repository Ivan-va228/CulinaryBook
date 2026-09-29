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
        image = R.drawable.pizza_salami,
        category = "Піца",
        difficulty = "Легко",
        time = "30 хв"
    ),
    Recipe(
        id = 2,
        name = "Млинці",
        image = R.drawable.pancakes,
        category = "Сніданок",
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
    ),
    Recipe(
        id = 4,
        name = "Піца 4 сира",
        image = R.drawable.pizza_4chesse,
        category = "Піца",
        difficulty = "Легко",
        time = "25 хв"
    ),
    Recipe(
        id = 5,
        name = "Сирники",
        image = R.drawable.syrniki,
        category = "Десерт",
        difficulty = "Важко",
        time = "40 хв"
    ),
    Recipe(
        id = 6,
        name = "Яєчня",
        image = R.drawable.eggs,
        category = "Сніданок",
        difficulty = "Легко",
        time = "10 хв"
    )
)