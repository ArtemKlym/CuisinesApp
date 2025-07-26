package com.artemklymenko.search.domain.use_cases.local

import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(recipeDomain: RecipeDomain) = flow<Unit> {
        searchRepository.deleteRecipe(recipeDomain)
    }.flowOn(Dispatchers.IO)
}