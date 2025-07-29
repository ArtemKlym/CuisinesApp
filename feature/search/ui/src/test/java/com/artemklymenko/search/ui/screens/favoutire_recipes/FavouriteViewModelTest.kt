package com.artemklymenko.search.ui.screens.favoutire_recipes

import com.artemklymenko.search.domain.use_cases.local.DeleteRecipeUseCase
import com.artemklymenko.search.domain.use_cases.local.GetAllRecipesFromLocalDbUseCase
import com.artemklymenko.search.ui.screens.MainDispatcherRule
import com.artemklymenko.search.ui.screens.getRecipeList
import com.artemklymenko.search.ui.screens.getRecipeResponse
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
class FavouriteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val deleteRecipeUseCase: DeleteRecipeUseCase = mock()
    private val getRecipeDetailUseCase: GetAllRecipesFromLocalDbUseCase = mock()
    private lateinit var viewModel: FavouriteViewModel

    companion object {
        private const val ID_STRING = "1"
    }

    @Before
    fun setup() {
        viewModel = FavouriteViewModel(getRecipeDetailUseCase, deleteRecipeUseCase)
        `when`(getRecipeDetailUseCase.invoke())
            .thenReturn(flowOf( getRecipeList()))
    }

    @Test
    fun `test alphabetical sort returns true`() = runTest {
        viewModel.onEvent(FavouriteEvent.AlphabeticalSort)
        advanceUntilIdle()
        assertEquals(
            getRecipeList().sortedBy { it.strMeal },
            viewModel.uiState.value.data
        )
    }

    @Test
    fun `test less ingredient sort returns true`() = runTest {
        viewModel.onEvent(FavouriteEvent.LessIngredientSort)
        advanceUntilIdle()
        assertEquals(
            getRecipeList().sortedBy { it.strInstructions.length },
            viewModel.uiState.value.data
        )
    }

    @Test
    fun `test reset sort returns true`() = runTest {
        viewModel.onEvent(FavouriteEvent.ResetSort)
        advanceUntilIdle()
        assertEquals(
            getRecipeList(),
            viewModel.uiState.value.data
        )
    }

    @Test
    fun `test navigation to details`() = runTest {
        viewModel.onEvent(FavouriteEvent.GoToDetails(ID_STRING))
        val event = viewModel.navigation.first()
        assert(event is FavouriteNavigationEvent.GoToRecipeDetailScreen)
    }

    @Test
    fun `delete recipe returns true`() = runTest {
        val recipe = getRecipeResponse()
        val recipeDb = mutableListOf(recipe)
        `when`(deleteRecipeUseCase.invoke(recipe))
            .then {
                recipeDb.remove(recipe)
                flowOf(Unit)
            }
        viewModel.onEvent(FavouriteEvent.DeleteRecipe(recipe))
        assert(recipeDb.isEmpty())
    }
}