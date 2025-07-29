package com.artemklymenko.search.domain.use_cases.remote

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetAllRecipeUseCaseTest {

    companion object {
        private const val QUERY_STRING = "chicken"
    }

    private val searchRepository: SearchRepository = mock()

    private lateinit var useCase: GetAllRecipeUseCase

    @Before
    fun setup() {
        useCase = GetAllRecipeUseCase(searchRepository)
    }

    @Test
    fun `get a valid recipe use-case`() = runTest {
        `when`(searchRepository.getRecipes(QUERY_STRING))
            .thenReturn(NetworkResult.Success(getRecipeResponse()))

        val response = useCase.invoke(QUERY_STRING)
        assertEquals(NetworkResult.Success(getRecipeResponse()), response.first())
    }

    @Test
    fun `test failure recipe use-case`() = runTest {
        `when`(searchRepository.getRecipes(QUERY_STRING))
            .thenReturn(NetworkResult.Error(NetworkError.SERIALIZATION_ERROR))

        val response = useCase.invoke(QUERY_STRING)
        assertEquals(NetworkResult.Error(NetworkError.SERIALIZATION_ERROR), response.first())
    }
}

private fun getRecipeResponse(): List<RecipeDomain> {
    return listOf(
        RecipeDomain(
            idMeal = "idMeal",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "12",
        ),
        RecipeDomain(
            idMeal = "idMeal",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "123",
        )
    )
}