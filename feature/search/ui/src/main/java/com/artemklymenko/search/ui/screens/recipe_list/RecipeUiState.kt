package com.artemklymenko.search.ui.screens.recipe_list

import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain

data class RecipeUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText = UiText.Idle,
    val data: List<RecipeDomain>? = null
)
