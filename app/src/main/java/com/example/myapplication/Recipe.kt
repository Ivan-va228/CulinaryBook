
package com.example.myapplication

data class Recipe(
    val id: Int,
    val name: String,
    val image: Int,
    val category: String,
    val difficulty: String,
    val time: String,
    val ingredients: String,
    val instructions: String
)

val recipes = listOf(
    Recipe(
        id = 1,
        name = "Піца Салямі",
        image = R.drawable.pizza_salami,
        category = "Піца",
        difficulty = "Легко",
        time = "30 хв",
        ingredients = "Тісто, томатний соус, сир, салямі",
        instructions = "1. Розкачати тісто.\n2. Додати соус, сир і салямі.\n3. Випікати до готовності."
    ),
    Recipe(
        id = 2,
        name = "Млинці",
        image = R.drawable.pancakes,
        category = "Сніданок",
        difficulty = "Легко",
        time = "25 хв",
        ingredients = "Молоко, яйця, борошно, цукор, сіль",
        instructions = "1. Змішати всі інгредієнти.\n2. Виливати тісто на розігріту сковороду.\n3. Обсмажити млинці з обох боків."
    ),
    Recipe(
        id = 3,
        name = "Чізкейк",
        image = R.drawable.cheesecake,
        category = "Десерт",
        difficulty = "Важко",
        time = "60 хв",
        ingredients = "Вершковий сир, печиво, масло, цукор, яйця",
        instructions = "1. Подрібнити печиво та змішати з маслом.\n2. Додати сирну начинку.\n3. Випікати до готовності й охолодити."
    ),
    Recipe(
        id = 4,
        name = "Піца 4 сира",
        image = R.drawable.pizza_4chesse,
        category = "Піца",
        difficulty = "Легко",
        time = "25 хв",
        ingredients = "Тісто, моцарела, пармезан, горгонзола, твердий сир",
        instructions = "1. Розкачати тісто.\n2. Викласти чотири види сиру.\n3. Випікати до готовності."
    ),
    Recipe(
        id = 5,
        name = "Сирники",
        image = R.drawable.syrniki,
        category = "Десерт",
        difficulty = "Важко",
        time = "40 хв",
        ingredients = "Кисломолочний сир, яйце, борошно, цукор",
        instructions = "1. Змішати сир, яйце, борошно та цукор.\n2. Сформувати сирники.\n3. Обсмажити з обох боків до готовності."
    ),
    Recipe(
        id = 6,
        name = "Яєчня",
        image = R.drawable.eggs,
        category = "Сніданок",
        difficulty = "Легко",
        time = "10 хв",
        ingredients = "Яйця, олія або масло, сіль",
        instructions = "1. Розігріти сковороду з олією або маслом.\n2. Розбити яйця на сковороду.\n3. Смажити до бажаної готовності."
    )
)
