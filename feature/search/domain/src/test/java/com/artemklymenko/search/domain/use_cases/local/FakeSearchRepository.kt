package com.artemklymenko.search.domain.use_cases.local

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSearchRepository: SearchRepository {

    private val database = mutableListOf<RecipeDomain>()
    private val list = mutableListOf<RecipeDetailsDomain>()

    override suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>> {
        return NetworkResult.Success(database)
    }

    override suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain> {
        return NetworkResult.Success(list.first())
    }

    override suspend fun insertRecipe(recipeDomain: RecipeDomain) {
        database.add(recipeDomain)
    }

    override suspend fun deleteRecipe(recipeDomain: RecipeDomain) {
        database.remove(recipeDomain)
    }

    override fun getAllRecipes(): Flow<List<RecipeDomain>> {
        return flowOf(database)
    }
}