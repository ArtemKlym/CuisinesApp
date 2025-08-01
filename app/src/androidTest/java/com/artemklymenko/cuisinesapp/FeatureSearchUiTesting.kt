package com.artemklymenko.cuisinesapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.artemklymenko.cuisinesapp.di.DatabaseModule
import com.artemklymenko.cuisinesapp.utils.getRecipeResponse
import com.artemklymenko.search.data.di.SearchDataModule
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListScreenTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(SearchDataModule::class, DatabaseModule::class)
class FeatureSearchUiTesting {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    companion object {
        private const val QUERY_STRING = "chicken"
    }

    @Test
    fun test_RecipeListSuccess() {
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }

            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal + 0))
        }
    }

    @Test
    fun test_RecipeListSuccess_recipeDetailsSuccess() {
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).assertIsDisplayed()
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal + 0)).performClick()
            waitForIdle()
            onNodeWithText(getRecipeResponse().first().strMeal).assertIsDisplayed()
        }
    }
}