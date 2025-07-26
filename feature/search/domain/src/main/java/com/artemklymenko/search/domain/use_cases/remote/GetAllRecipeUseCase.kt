package com.artemklymenko.search.domain.use_cases.remote

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(
    private val searchRepository: SearchRepository
){

    operator fun invoke(searchQuery: String) = flow<NetworkResult<List<RecipeDomain>>> {
        val response = searchRepository.getRecipes(searchQuery)
        emit(response)
    }.flowOn(Dispatchers.IO)
}