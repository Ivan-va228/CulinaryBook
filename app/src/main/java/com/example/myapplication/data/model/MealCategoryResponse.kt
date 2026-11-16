package com.example.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealCategoryResponse(
    @SerialName("categories")
    val categories: List<MealCategory>? = null
)

@Serializable
data class MealCategory(
    @SerialName("strCategory")
    val name: String? = null
)