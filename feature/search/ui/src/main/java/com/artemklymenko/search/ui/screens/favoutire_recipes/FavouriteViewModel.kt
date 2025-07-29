package com.artemklymenko.search.ui.screens.favoutire_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.domain.use_cases.local.DeleteRecipeUseCase
import com.artemklymenko.search.domain.use_cases.local.GetAllRecipesFromLocalDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getAllRecipesFromLocalDbUseCase: GetAllRecipesFromLocalDbUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private var originalList = mutableListOf<RecipeDomain>()

    private val _uiState = MutableStateFlow(FavouriteUiState())
    val uiState: StateFlow<FavouriteUiState> = _uiState.asStateFlow()

    private val _navigation = Channel<FavouriteNavigationEvent>()
    val navigation: Flow<FavouriteNavigationEvent> = _navigation.receiveAsFlow()

    init {
        getRecipeList()
    }

    fun onEvent(event: FavouriteEvent) {
        when (event) {
            FavouriteEvent.AlphabeticalSort -> alphabeticalSort()
            FavouriteEvent.LessIngredientSort -> lessIngredientsSort()
            FavouriteEvent.ResetSort -> resetSort()
            is FavouriteEvent.DeleteRecipe -> deleteRecipe(event.recipeDomain)
            is FavouriteEvent.GoToDetails -> viewModelScope.launch {
                _navigation.send(FavouriteNavigationEvent.GoToRecipeDetailScreen(event.id))
            }
        }
    }

    private fun deleteRecipe(recipeDomain: RecipeDomain) = deleteRecipeUseCase.invoke(recipeDomain)
        .launchIn(viewModelScope)

    private fun getRecipeList() = viewModelScope.launch {
        _uiState.update {
            FavouriteUiState(isLoading = true, errorMessage = UiText.Idle)
        }
        getAllRecipesFromLocalDbUseCase.invoke().collectLatest { recipes ->
            originalList = recipes.toMutableList()
            _uiState.update {
                it.copy(
                    data = recipes,
                    isLoading = false,
                    errorMessage = UiText.Idle
                )
            }
        }
    }

    private fun alphabeticalSort() = _uiState.update { state ->
        state.copy(
            data = originalList.sortedBy { it.strMeal }
        )
    }

    private fun lessIngredientsSort() = _uiState.update { state ->
        state.copy(
            data = originalList.sortedBy { it.strInstructions.length }
        )
    }

    private fun resetSort() {
        _uiState.update {
            it.copy(data = originalList)
        }
    }
}