package com.artemklymenko.search.ui.screens.recipe_list

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.use_cases.remote.GetAllRecipeUseCase
import com.artemklymenko.search.ui.screens.MainDispatcherRule
import com.artemklymenko.search.ui.screens.getRecipeList
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
class RecipeListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllRecipeUseCase: GetAllRecipeUseCase = mock()
    private lateinit var viewModel: RecipeListViewModel

    companion object {
        private const val QUERY_STRING = "chicken"
        private const val ID_STRING = "1"
    }

    @Before
    fun setup() {
        viewModel = RecipeListViewModel(getAllRecipeUseCase)
    }

    @Test
    fun `get all recipes returns true`() = runTest(mainDispatcherRule.getTestDispatcher()) {
        `when`(getAllRecipeUseCase.invoke(QUERY_STRING))
            .thenReturn(flowOf(NetworkResult.Success(getRecipeList())))

        viewModel.onEvent(RecipeEvent.SearchRecipe(QUERY_STRING))
        viewModel.onEvent(RecipeEvent.OnSearchTriggered)

        advanceUntilIdle()

        assertEquals(getRecipeList(), viewModel.uiState.value.data)
    }

    @Test
    fun `test failed to get all recipes returns true`() = runTest {
        `when`(getAllRecipeUseCase.invoke(QUERY_STRING))
            .thenReturn(flowOf(NetworkResult.Error(NetworkError.NO_INTERNET)))
        viewModel.onEvent(RecipeEvent.SearchRecipe(QUERY_STRING))
        viewModel.onEvent(RecipeEvent.OnSearchTriggered)

        advanceUntilIdle()

        assertEquals(UiText.RemoteString(NetworkError.NO_INTERNET.toString()), viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test navigation to recipe details screen returns true`() = runTest {
        viewModel.onEvent(RecipeEvent.GoToRecipeDetails(ID_STRING))
        val event = viewModel.navigation.first()
        assert(event is RecipeNavigationEvent.GoToRecipeDetails)
    }

    @Test
    fun `test navigation to favourite screen returns true`() = runTest {
        viewModel.onEvent(RecipeEvent.GoToFavouriteScreen)
        val event = viewModel.navigation.first()
        assert(event is RecipeNavigationEvent.GoToFavoriteScreen)
    }
}