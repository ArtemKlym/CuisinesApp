package com.artemklymenko.search.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeDomain(
    @PrimaryKey(autoGenerate = false)
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val strCategory: String,
    val strTags: String,
    val strYoutube: String,
    val strInstructions: String,
)
