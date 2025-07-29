package com.artemklymenko.search.ui.screens.recipe_details

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.use_cases.local.DeleteRecipeUseCase
import com.artemklymenko.search.domain.use_cases.local.InsertRecipeUseCase
import com.artemklymenko.search.domain.use_cases.remote.GetRecipeDetailUseCase
import com.artemklymenko.search.ui.components.toRecipeDomain
import com.artemklymenko.search.ui.screens.MainDispatcherRule
import com.artemklymenko.search.ui.screens.getRecipeDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRecipeDetailUseCase: GetRecipeDetailUseCase = mock()
    private val insertRecipeUseCase: InsertRecipeUseCase = mock()
    private val deleteRecipeUseCase: DeleteRecipeUseCase = mock()
    private lateinit var viewModel: RecipeDetailViewModel

    companion object {
        private const val ID_STRING = "1"
        private const val MEDIA_URL = "strYoutube"
    }

    @Before
    fun setup() {
        viewModel = RecipeDetailViewModel(getRecipeDetailUseCase, deleteRecipeUseCase, insertRecipeUseCase)
    }

    @Test
    fun `get a recipe details returns true`() = runTest {
        `when`(getRecipeDetailUseCase.invoke(ID_STRING))
            .thenReturn(flowOf(NetworkResult.Success(getRecipeDetails())))
        viewModel.onEvent(RecipeDetailEvent.GetRecipeDetail(ID_STRING))
        advanceUntilIdle()
        assertEquals(getRecipeDetails(), viewModel.uiState.value.data)
    }

    @Test
    fun `test failed to get a recipe details returns true`() = runTest {
        `when`(getRecipeDetailUseCase.invoke(ID_STRING))
            .thenReturn(flowOf(NetworkResult.Error(NetworkError.NO_INTERNET)))
        viewModel.onEvent(RecipeDetailEvent.GetRecipeDetail(ID_STRING))
        advanceUntilIdle()
        assertEquals(UiText.RemoteString(NetworkError.NO_INTERNET.toString()), viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test navigation to a media player screen returns true`() = runTest {
        viewModel.onEvent(RecipeDetailEvent.GoToMediaPlayer(MEDIA_URL))
        val event = viewModel.navigation.first()
        assert(event is RecipeDetailNavigationEvent.GoToMediaPlayer)
    }

    @Test
    fun `test navigation to a recipe list screen returns true`() = runTest {
        viewModel.onEvent(RecipeDetailEvent.GoToRecipeListScreen)
        val event = viewModel.navigation.first()
        assert(event is RecipeDetailNavigationEvent.GoToNavigationList)
    }

    @Test
    fun `insert a valid recipe use-case returns true`() = runTest {
        val recipeDb = mutableListOf<RecipeDomain>()
        val recipe = getRecipeDetails().toRecipeDomain()
        `when`(insertRecipeUseCase.invoke(recipe))
            .then {
                recipeDb.add(recipe)
                flowOf(Unit)
            }
        viewModel.onEvent(RecipeDetailEvent.InsertRecipe(getRecipeDetails()))
        assert(recipeDb.contains(recipe))
    }

    @Test
    fun `delete a recipe use-case returns true`() = runTest {
        val recipe = getRecipeDetails().toRecipeDomain()
        val recipeDb =  mutableListOf(recipe)
        `when`(deleteRecipeUseCase.invoke(recipe))
            .then {
                recipeDb.remove(recipe)
                flowOf(Unit)
            }
        viewModel.onEvent(RecipeDetailEvent.DeleteRecipe(getRecipeDetails()))
        assert(recipeDb.isEmpty())
    }
}