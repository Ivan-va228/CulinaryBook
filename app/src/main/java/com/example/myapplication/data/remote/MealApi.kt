package com.example.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.myapplication.data.model.MealListResponse

interface MealApi {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") name: String
    ): MealListResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealListResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): MealListResponse
}