package com.artemklymenko.search.data.repository

import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.domain.model.RecipeDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRecipeDao: RecipeDao {

    private val list = mutableListOf<RecipeDomain>()

    override suspend fun insertRecipe(recipeDomain: RecipeDomain) {
        list.add(recipeDomain)
    }

    override suspend fun deleteRecipe(recipeDomain: RecipeDomain) {
        list.remove(recipeDomain)
    }

    override suspend fun updateRecipe(recipeDomain: RecipeDomain) {
        // No real logic for this test
    }

    override fun getAllRecipes(): Flow<List<RecipeDomain>> {
        return flowOf(list)
    }
}