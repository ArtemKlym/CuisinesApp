package com.artemklymenko.search.domain.use_cases.local

import com.artemklymenko.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetAllRecipesFromLocalDbUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    operator fun invoke() = searchRepository.getAllRecipes()
}