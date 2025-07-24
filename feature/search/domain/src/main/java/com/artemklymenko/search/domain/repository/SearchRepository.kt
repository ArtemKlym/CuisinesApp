package com.artemklymenko.search.domain.repository

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain

interface SearchRepository {

    suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>>

    suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain>
}