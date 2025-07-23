package com.artemklymenko.search.data.repository

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.data.mappers.toDomain
import com.artemklymenko.search.data.mappers.toNetworkResult
import com.artemklymenko.search.data.remote.SearchApiService
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import java.io.IOException
import java.net.UnknownHostException

class SearchRepositoryImpl(
    private val searchApiService: SearchApiService
) : SearchRepository {

    override suspend fun getRecipes(searchQuery: String): NetworkResult<List<RecipeDomain>> {
        return try {
            val response = searchApiService.getRecipes(searchQuery)
            when (val result = response.toNetworkResult()) {
                is NetworkResult.Success -> {
                    result.data.meals?.let {
                        NetworkResult.Success(it.toDomain())
                    } ?: NetworkResult.Error(NetworkError.SERIALIZATION_ERROR)
                }

                is NetworkResult.Error -> result
            }
        } catch (e: UnknownHostException) {
            NetworkResult.Error(NetworkError.NO_INTERNET)
        } catch (e: IOException) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getRecipeDetails(id: String): NetworkResult<RecipeDetailsDomain> {
        return try {
            val response = searchApiService.getRecipeDetails(recipeId = id)
            when (val result = response.toNetworkResult()) {
                is NetworkResult.Success -> {
                    val meal = result.data.meals?.firstOrNull()
                    if (meal != null) {
                        NetworkResult.Success(meal.toDomain())
                    } else {
                        NetworkResult.Error(NetworkError.SERIALIZATION_ERROR)
                    }
                }
                is NetworkResult.Error -> result
            }
        } catch (e: UnknownHostException) {
            NetworkResult.Error(NetworkError.NO_INTERNET)
        } catch (e: IOException) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }
}