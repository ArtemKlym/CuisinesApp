package com.artemklymenko.search.ui.screens.favoutire_recipes

import com.artemklymenko.search.domain.model.RecipeDomain

sealed interface FavouriteEvent {
    data object AlphabeticalSort: FavouriteEvent
    data object LessIngredientSort: FavouriteEvent
    data object ResetSort: FavouriteEvent
    data class DeleteRecipe(val recipeDomain: RecipeDomain): FavouriteEvent
    data class GoToDetails(val id: String): FavouriteEvent
}