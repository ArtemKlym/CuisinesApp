package com.artemklymenko.cuisinesapp.repository

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFailureRepositoryImpl: SearchRepository {

    private val dbFlow = MutableStateFlow(emptyList<RecipeDomain>())
    private val list = mutableListOf<RecipeDomain>()

    fun reset() {
        list.clear()
    }

    override suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>> {
        return NetworkResult.Error(NetworkError.NO_INTERNET)
    }

    override suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain> {
        return NetworkResult.Error(NetworkError.NO_INTERNET)
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