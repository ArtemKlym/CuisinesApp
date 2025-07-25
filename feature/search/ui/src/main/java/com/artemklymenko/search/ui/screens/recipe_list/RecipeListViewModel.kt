package com.artemklymenko.search.ui.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemklymenko.common.utils.NetworkResult
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.use_cases.remote.GetAllRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getAllRecipeUseCase: GetAllRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    private val _navigation = Channel<RecipeNavigationEvent>()
    val navigation: Flow<RecipeNavigationEvent> = _navigation.receiveAsFlow()

    init {
        observeDebouncedQuery()
    }

    fun onEvent(event: RecipeEvent) {
        when (event) {
            is RecipeEvent.SearchRecipe -> {
                queryFlow.value = event.querySearch
            }
            is RecipeEvent.OnSearchTriggered -> {
                search(queryFlow.value)
            }
            RecipeEvent.NetworkRestored -> {
                if (queryFlow.value.isNotBlank()) {
                    search(queryFlow.value)
                }
            }
            is RecipeEvent.GoToRecipeDetails -> {
                viewModelScope.launch {
                    _navigation.send(RecipeNavigationEvent.GoToRecipeDetails(event.id))
                }
            }

            RecipeEvent.GoToFavouriteScreen -> viewModelScope.launch {
                _navigation.send(RecipeNavigationEvent.GoToFavoriteScreen)
            }
        }
    }

    private fun search(querySearch: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true, errorMessage = UiText.Idle)
        }
        getAllRecipeUseCase.invoke(querySearch).collect{ result ->
                when (result) {
                    is NetworkResult.Error -> {
                        _uiState.update { state ->
                            state.copy(
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

    @OptIn(FlowPreview::class)
    private fun observeDebouncedQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(700)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest {
                    onEvent(RecipeEvent.OnSearchTriggered)
                }
        }
    }
}
