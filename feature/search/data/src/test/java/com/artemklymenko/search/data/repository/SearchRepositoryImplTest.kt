package com.artemklymenko.search.data.repository

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.data.mappers.toDomain
import com.artemklymenko.search.data.model.RecipeDTO
import com.artemklymenko.search.data.model.RecipeDetailsResponse
import com.artemklymenko.search.data.model.RecipeResponse
import com.artemklymenko.search.data.remote.SearchApiService
import com.artemklymenko.search.domain.model.RecipeDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class SearchRepositoryImplTest {

    companion object {
        private const val QUERY_STRING = "chicken"
        private const val ID_STRING = "1"
    }

    private val searchApiService: SearchApiService = mock()
    private val recipeDao: RecipeDao = mock()

    private lateinit var searchRepository: SearchRepositoryImpl
    private lateinit var searchFakeDaoRepo: SearchRepositoryImpl

    @Before
    fun setup() {
        searchRepository = SearchRepositoryImpl(searchApiService, recipeDao)
        searchFakeDaoRepo = SearchRepositoryImpl(searchApiService, FakeRecipeDao())
    }

    @Test
    fun `get a valid recipe response`() = runTest {
        `when`(searchApiService.getRecipes(QUERY_STRING))
            .thenReturn(Response.success(200, getRecipeResponse()))

        val response = searchRepository.getRecipes(QUERY_STRING)
        assertEquals(NetworkResult.Success(getRecipeResponse().meals?.toDomain()), response)
    }

    @Test
    fun `get a valid recipe details response`() = runTest {
        `when`(searchApiService.getRecipeDetails(ID_STRING))
            .thenReturn(Response.success(200, getRecipeDetails()))

        val response = searchRepository.getRecipeDetails(ID_STRING)
        val expected = NetworkResult.Success(getRecipeDetails().meals?.first()?.toDomain())
        assertEquals(expected, response)
    }

    @Test
    fun `get an empty list recipe details returns SERIALIZATION_ERROR error`() = runTest {
        `when`(searchApiService.getRecipeDetails(ID_STRING))
            .thenReturn(Response.success(200, RecipeDetailsResponse(meals = emptyList())))

        val response = searchRepository.getRecipeDetails(ID_STRING)
        val errorMessage = NetworkResult.Error(NetworkError.SERIALIZATION_ERROR)
        assertEquals(errorMessage, response)
    }

    @Test
    fun `test if meals is null`() = runTest {
        `when`(searchApiService.getRecipes(QUERY_STRING))
            .thenReturn(Response.success(200, getRecipeResponseWithNullMeals()))

        val response = searchRepository.getRecipes(QUERY_STRING)
        val errorMessage = NetworkResult.Error(NetworkError.SERIALIZATION_ERROR)
        assertEquals(errorMessage, response)
    }

    @Test
    fun `test 408 returns REQUEST_TIMEOUT error`() = runTest {
        val errorBody = "Request Timeout"
            .toResponseBody("application/json".toMediaTypeOrNull())

        `when`(searchApiService.getRecipes(QUERY_STRING))
            .thenReturn(Response.error(408, errorBody))

        val response = searchRepository.getRecipes(QUERY_STRING)
        val errorMessage = NetworkResult.Error(NetworkError.REQUEST_TIMEOUT)
        assertEquals(errorMessage, response)
    }

    @Test
    fun `test 429 returns TOO_MANY_REQUESTS error`() = runTest {
        val errorBody = "Too many requests"
            .toResponseBody("application/json".toMediaTypeOrNull())

        `when`(searchApiService.getRecipes(QUERY_STRING))
            .thenReturn(Response.error(429, errorBody))

        val response = searchRepository.getRecipes(QUERY_STRING)
        val errorMessage = NetworkResult.Error(NetworkError.TOO_MANY_REQUESTS)
        assertEquals(errorMessage, response)
    }

    @Test
    fun `test 500 returns SERVER_ERROR error`() = runTest {
        val errorBody = "Server error"
            .toResponseBody("application/json".toMediaTypeOrNull())

        `when`(searchApiService.getRecipes(QUERY_STRING))
            .thenReturn(Response.error(500, errorBody))

        val response = searchRepository.getRecipes(QUERY_STRING)
        val errorMessage = NetworkResult.Error(NetworkError.SERVER_ERROR)
        assertEquals(errorMessage, response)
    }

    @Test
    fun `test no internet connection returns NO_INTERNET error`() = runTest {
        val exception = UnknownHostException()

        Mockito.doAnswer {
            throw exception
        }.`when`(searchApiService).getRecipes(QUERY_STRING)

        val result = searchRepository.getRecipes(QUERY_STRING)
        assertEquals(NetworkResult.Error(NetworkError.NO_INTERNET), result)
    }

    @Test
    fun `test io exception returns UNKNOWN error`() = runTest {
        val ioException = IOException()

        Mockito.doAnswer {
            throw ioException
        }.`when`(searchApiService).getRecipes(QUERY_STRING)

        val result = searchRepository.getRecipes(QUERY_STRING)
        assertEquals(NetworkResult.Error(NetworkError.UNKNOWN), result)
    }

    @Test
    fun `test insert a recipe to database`() = runTest {
        val recipe = getRecipeResponse().meals?.toDomain()?.first()
        searchFakeDaoRepo.insertRecipe(recipe!!)
        assertEquals(recipe, searchFakeDaoRepo.getAllRecipes().first().first())
    }

    @Test
    fun `test remove a recipe from database`() = runTest {
        val recipe = getRecipeResponse().meals?.toDomain()?.first()
        searchFakeDaoRepo.insertRecipe(recipe!!)
        val list = searchFakeDaoRepo.getAllRecipes().first().first()
        assertEquals(recipe, list)
        searchFakeDaoRepo.deleteRecipe(recipe)
        assertEquals(emptyList<RecipeDomain>(), searchFakeDaoRepo.getAllRecipes().last())
    }
}

private fun getRecipeResponse(): RecipeResponse {
    return RecipeResponse(
        meals = listOf(
            RecipeDTO(
                dateModified = null,
                idMeal = "idMeal",
                strArea = "India",
                strCategory = "category",
                strYoutube = "strYoutube",
                strTags = "tag1,tag2",
                strMeal = "Chicken",
                strSource = "strSource",
                strMealThumb = "strMealThumb",
                strInstructions = "strInstructions",
                strCreativeCommonsConfirmed = null,
                strIngredient1 = null,
                strIngredient2 = null,
                strIngredient3 = null,
                strIngredient4 = null,
                strIngredient5 = null,
                strIngredient6 = null,
                strIngredient7 = null,
                strIngredient8 = null,
                strIngredient9 = null,
                strIngredient10 = null,
                strIngredient11 = null,
                strIngredient12 = null,
                strIngredient13 = null,
                strIngredient14 = null,
                strIngredient15 = null,
                strIngredient16 = null,
                strIngredient17 = null,
                strIngredient18 = null,
                strIngredient19 = null,
                strIngredient20 = null,
                strMeasure1 = null,
                strMeasure2 = null,
                strMeasure3 = null,
                strMeasure4 = null,
                strMeasure5 = null,
                strMeasure6 = null,
                strMeasure7 = null,
                strMeasure8 = null,
                strMeasure9 = null,
                strMeasure10 = null,
                strMeasure11 = null,
                strMeasure12 = null,
                strMeasure13 = null,
                strMeasure14 = null,
                strMeasure15 = null,
                strMeasure16 = null,
                strMeasure17 = null,
                strMeasure18 = null,
                strMeasure19 = null,
                strMeasure20 = null,
                strImageSource = "empty",
                strMealAlternate = null
            )
        )
    )
}

private fun getRecipeResponseWithNullMeals(): RecipeResponse {
    return RecipeResponse(meals = null)
}

private fun getRecipeDetails(): RecipeDetailsResponse {
    return RecipeDetailsResponse(
        meals = listOf(
            getRecipeResponse().meals?.first()!!
        )
    )
}