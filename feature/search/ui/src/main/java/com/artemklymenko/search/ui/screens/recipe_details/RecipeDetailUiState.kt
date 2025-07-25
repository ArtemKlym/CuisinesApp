package com.artemklymenko.search.ui.screens.recipe_details

import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDetailsDomain

data class RecipeDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText = UiText.Idle,
    val data: RecipeDetailsDomain? = null
)
