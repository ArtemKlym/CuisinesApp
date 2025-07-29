package com.artemklymenko.search.domain.use_cases.remote

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetRecipeDetailUseCaseTest {

    private val searchRepository: SearchRepository = mock()
    private lateinit var useCase: GetRecipeDetailUseCase

    companion object {
        private const val ID_STRING = "1"
    }

    @Before
    fun setup() {
        useCase = GetRecipeDetailUseCase(searchRepository)
    }

    @Test
    fun `get a valid recipe detail use-case`() = runTest {
        `when`(searchRepository.getRecipeDetails(ID_STRING))
            .thenReturn(NetworkResult.Success(getRecipeDetails()))
        val response = useCase.invoke(ID_STRING)
        assertEquals(NetworkResult.Success(getRecipeDetails()), response.first())
    }

    @Test
    fun `test failure recipe detail use-case`() = runTest {
        `when`(searchRepository.getRecipeDetails(ID_STRING))
            .thenReturn(NetworkResult.Error(NetworkError.SERIALIZATION_ERROR))
        val response = useCase.invoke(ID_STRING)
        assertEquals(NetworkResult.Error(NetworkError.SERIALIZATION_ERROR), response.first())
    }
}

private fun getRecipeDetails(): RecipeDetailsDomain {
    return RecipeDetailsDomain(
        idMeal = "idMeal",
        strArea = "India",
        strCategory = "category",
        strYoutube = "strYoutube",
        strTags = "tag1,tag2",
        strMeal = "Chicken",
        strMealThumb = "strMealThumb",
        strInstructions = "strInstructions",
        ingredientsPair = listOf(Pair("Ingredients", "Measure"))
    )
}