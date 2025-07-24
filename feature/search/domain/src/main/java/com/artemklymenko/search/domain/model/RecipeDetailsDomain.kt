package com.artemklymenko.search.domain.model


data class RecipeDetailsDomain(
    val idMeal: String,
    val strMeal: String,
    val strArea: String,
    val strMealThumb: String,
    val strCategory: String,
    val strTags: String,
    val strYoutube: String,
    val strInstructions: String,
    val ingredientsPair: List<Pair<String, String>>
)
