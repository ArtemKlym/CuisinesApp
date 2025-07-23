package com.artemklymenko.search.data.remote

import com.artemklymenko.search.data.model.RecipeDetailsResponse
import com.artemklymenko.search.data.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("api/json/v1/1/search.php")
    suspend fun getRecipes(
        @Query("s") searchQuery: String
    ): Response<RecipeResponse>

    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") recipeId: String
    ): Response<RecipeDetailsResponse>
}