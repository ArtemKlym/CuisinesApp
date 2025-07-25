package com.artemklymenko.search.ui.screens.favoutire_recipes

import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain

data class FavouriteUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText = UiText.Idle,
    val data: List<RecipeDomain>? = null
)
