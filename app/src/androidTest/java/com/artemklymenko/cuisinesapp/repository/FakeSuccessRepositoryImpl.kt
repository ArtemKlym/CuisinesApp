package com.artemklymenko.cuisinesapp.repository

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.cuisinesapp.utils.getRecipeDetailsList
import com.artemklymenko.cuisinesapp.utils.getRecipeResponse
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSuccessRepositoryImpl: SearchRepository {

    private val dbFlow = MutableStateFlow(emptyList<RecipeDomain>())
    private val list = mutableListOf<RecipeDomain>()

    fun reset() {
        list.clear()
    }

    override suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>> {
        return NetworkResult.Success(getRecipeResponse())
    }

    override suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain> {
       return NetworkResult.Success(getRecipeDetailsList().first())
    }

    override suspend fun insertRecipe(recipeDomain: RecipeDomain) {
        list.add(recipeDomain)
        dbFlow.value = list.toList()
    }

    override suspend fun deleteRecipe(recipeDomain: RecipeDomain) {
        list.remove(recipeDomain)
        dbFlow.value = list.toList()
    }

    override fun getAllRecipes(): Flow<List<RecipeDomain>> {
        return dbFlow
    }
}