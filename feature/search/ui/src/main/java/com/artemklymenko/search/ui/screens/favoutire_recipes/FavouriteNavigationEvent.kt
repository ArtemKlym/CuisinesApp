package com.artemklymenko.search.ui.screens.favoutire_recipes

sealed interface FavouriteNavigationEvent {
    data class GoToRecipeDetailScreen(val id: String): FavouriteNavigationEvent
}