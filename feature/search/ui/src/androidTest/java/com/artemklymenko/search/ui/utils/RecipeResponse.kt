package com.artemklymenko.search.ui.utils

import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain

fun getRecipeResponse(): List<RecipeDomain> {
    return listOf(
        RecipeDomain(
            idMeal = "idMeal 1",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "12345",
        ),
        RecipeDomain(
            idMeal = "idMeal 2",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag3,tag4",
            strMeal = "Kadai Paneer",
            strMealThumb = "strMealThumb",
            strInstructions = "123",
        )
    )
}

fun getRecipeDetailsList(): List<RecipeDetailsDomain> {
    return listOf(
        RecipeDetailsDomain(
            idMeal = "idMeal 1",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Chicken",
            strMealThumb = "strMealThumb",
            strInstructions = "strInstructions",
            ingredientsPair = listOf(Pair("Ingredients", "Measure"))
        ),
        RecipeDetailsDomain(
            idMeal = "idMeal 2",
            strArea = "India",
            strCategory = "category",
            strYoutube = "strYoutube",
            strTags = "tag1,tag2",
            strMeal = "Kadai Paneer",
            strMealThumb = "strMealThumb",
            strInstructions = "123",
            ingredientsPair = listOf(Pair("Ingredients 2", "Measure 2"))
        )
    )
}


fun getRecipeDetails(): RecipeDetailsDomain {
    return RecipeDetailsDomain(
        idMeal = "idMeal",
        strArea = "India",
        strCategory = "category",
        strYoutube = "strYoutube",
        strTags = "tag1,tag2",
        strMeal = "Chicken",
        strMealThumb = "strMealThumb",
        strInstructions = "strInstructions",
        ingredientsPair = listOf(Pair("Ingredients", "Measure"))
    )
}