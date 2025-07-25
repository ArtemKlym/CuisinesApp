package com.artemklymenko.search.ui.screens.recipe_details

sealed interface RecipeDetailNavigationEvent {
    data object GoToNavigationList: RecipeDetailNavigationEvent
    data class GoToMediaPlayer(val youtubeUrl: String): RecipeDetailNavigationEvent
}