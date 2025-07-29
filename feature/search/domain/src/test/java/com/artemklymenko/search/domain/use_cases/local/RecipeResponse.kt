package com.artemklymenko.search.domain.use_cases.local

import com.artemklymenko.search.domain.model.RecipeDomain

fun getRecipeResponse(): RecipeDomain {
    return RecipeDomain(
        idMeal = "idMeal",
        strArea = "India",
        strCategory = "category",
        strYoutube = "strYoutube",
        strTags = "tag1,tag2",
        strMeal = "Chicken",
        strMealThumb = "strMealThumb",
        strInstructions = "12",
    )
}