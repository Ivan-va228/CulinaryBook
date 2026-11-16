package com.example.myapplication.data.model

data class Recipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val category: String,
    val area: String,
    val ingredients: String,
    val instructions: String,
    val tags: String,
    val youtube: String,
    val source: String
)