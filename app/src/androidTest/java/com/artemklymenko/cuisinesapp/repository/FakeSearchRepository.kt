package com.artemklymenko.cuisinesapp.repository

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.cuisinesapp.utils.getRecipeDetailsList
import com.artemklymenko.cuisinesapp.utils.getRecipeResponse
import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class FakeSearchRepository(
    private val recipeDao: RecipeDao
): SearchRepository {
    override suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>> {
       return NetworkResult.Success(getRecipeResponse())
    }

    override suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain> {
        return NetworkResult.Success(getRecipeDetailsList().first())
    }

    override suspend fun insertRecipe(recipeDomain: RecipeDomain) {
        recipeDao.insertRecipe(recipeDomain)
    }

    override suspend fun deleteRecipe(recipeDomain: RecipeDomain) {
        recipeDao.deleteRecipe(recipeDomain)
    }

    override fun getAllRecipes(): Flow<List<RecipeDomain>> {
        return recipeDao.getAllRecipes()
    }
}