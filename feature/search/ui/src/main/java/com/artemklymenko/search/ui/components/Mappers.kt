package com.artemklymenko.search.ui.components

import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.domain.model.RecipeDomain

fun RecipeDetailsDomain.toRecipeDomain(): RecipeDomain {
    return RecipeDomain(
        idMeal = idMeal,
        strMeal = strMeal,
        strTags = strTags,
        strMealThumb = strMealThumb,
        strArea = strArea,
        strInstructions = strInstructions,
        strCategory = strCategory,
        strYoutube = strYoutube
    )
}