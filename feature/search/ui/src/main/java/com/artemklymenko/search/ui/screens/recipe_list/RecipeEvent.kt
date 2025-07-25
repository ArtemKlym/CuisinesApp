package com.artemklymenko.search.ui.screens.recipe_list

sealed interface RecipeEvent {
    data class SearchRecipe(val querySearch: String): RecipeEvent
    data object OnSearchTriggered : RecipeEvent
    data object NetworkRestored: RecipeEvent
    data class GoToRecipeDetails(val id: String): RecipeEvent
    data object GoToFavouriteScreen: RecipeEvent
}