package com.artemklymenko.search.ui.screens.recipe_list

sealed interface RecipeNavigationEvent {
    data class GoToRecipeDetails(val id: String) : RecipeNavigationEvent
    data object GoToFavoriteScreen: RecipeNavigationEvent
}