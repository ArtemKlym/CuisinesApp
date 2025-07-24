package com.artemklymenko.search.domain.use_cases

import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipeDetailUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(id: String) = flow<NetworkResult<RecipeDetailsDomain>> {
        val response = searchRepository.getRecipeDetails(id)
        emit(response)
    }.flowOn(Dispatchers.IO)
}