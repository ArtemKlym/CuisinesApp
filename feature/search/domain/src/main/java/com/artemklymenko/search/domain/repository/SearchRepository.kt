package com.artemklymenko.search.domain.repository

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>>

    suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain>

    suspend fun insertRecipe(recipeDomain: RecipeDomain)

    suspend fun deleteRecipe(recipeDomain: RecipeDomain)

    fun getAllRecipes(): Flow<List<RecipeDomain>>
}