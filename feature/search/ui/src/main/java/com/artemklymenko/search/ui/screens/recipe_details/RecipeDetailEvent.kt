package com.artemklymenko.search.ui.screens.recipe_details

import com.artemklymenko.search.domain.model.RecipeDetailsDomain

sealed interface RecipeDetailEvent {
    data class GetRecipeDetail(val id: String): RecipeDetailEvent
    data object GoToRecipeListScreen: RecipeDetailEvent
    data class GoToMediaPlayer(val youtubeUrl: String): RecipeDetailEvent
    data class InsertRecipe(val recipeDetails: RecipeDetailsDomain): RecipeDetailEvent
    data class DeleteRecipe(val recipeDetails: RecipeDetailsDomain): RecipeDetailEvent
}