package com.artemklymenko.search.ui.screens.recipe_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.use_cases.local.DeleteRecipeUseCase
import com.artemklymenko.search.domain.use_cases.local.InsertRecipeUseCase
import com.artemklymenko.search.domain.use_cases.remote.GetRecipeDetailUseCase
import com.artemklymenko.search.ui.components.toRecipeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeDetailNavigationEvent>()
    val navigation: Flow<RecipeDetailNavigationEvent> = _navigation.receiveAsFlow()

    fun onEvent(event: RecipeDetailEvent) {
        when (event) {
            is RecipeDetailEvent.GetRecipeDetail -> recipeDetail(event.id)
            RecipeDetailEvent.GoToRecipeListScreen -> viewModelScope.launch {
                _navigation.send(RecipeDetailNavigationEvent.GoToNavigationList)
            }
            is RecipeDetailEvent.DeleteRecipe -> {
                deleteRecipeUseCase.invoke(event.recipeDetails.toRecipeDomain())
                    .launchIn(viewModelScope)
            }
            is RecipeDetailEvent.InsertRecipe -> {
                insertRecipeUseCase.invoke(event.recipeDetails.toRecipeDomain())
                    .launchIn(viewModelScope)
            }
            is RecipeDetailEvent.GoToMediaPlayer -> viewModelScope.launch {
                _navigation.send(RecipeDetailNavigationEvent.GoToMediaPlayer(event.youtubeUrl))
            }
        }
    }

    private fun recipeDetail(id: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true, errorMessage = UiText.Idle)
        }
        getRecipeDetailUseCase.invoke(id).collect { result ->
            when (result) {
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = UiText.RemoteString(result.error.toString())
                        )
                    }
                }

                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            data = result.data,
                            errorMessage = UiText.Idle
                        )
                    }
                }
            }
        }
    }
}