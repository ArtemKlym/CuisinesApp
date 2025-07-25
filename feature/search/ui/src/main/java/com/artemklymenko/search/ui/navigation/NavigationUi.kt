package com.artemklymenko.search.ui.navigation


import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.artemklymenko.common.navigation.FeatureApi
import com.artemklymenko.common.navigation.NavigationRoute
import com.artemklymenko.common.navigation.NavigationSubGraphRoute
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteEvent
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteScreen
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteViewModel
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailEvent
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailScreen
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailViewModel
import com.artemklymenko.search.ui.screens.recipe_list.RecipeEvent
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListScreen
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListViewModel

interface SearchFeatureApi: FeatureApi

class SearchFeatureApiImpl: SearchFeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.Search.route,
            startDestination = NavigationRoute.RecipeList.route
        ) {
            composable(route = NavigationRoute.RecipeList.route) {
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(
                    viewModel = viewModel,
                    navHostController = navHostController,
                    onDetailClick = { mealId ->
                        viewModel.onEvent(RecipeEvent.GoToRecipeDetails(id = mealId))
                    }
                )
            }

            composable(route = NavigationRoute.RecipeDetails.route) { mealArgumentId ->
                val viewModel = hiltViewModel<RecipeDetailViewModel>()
                val mealId = mealArgumentId.arguments?.getString("id")
                LaunchedEffect(mealId) {
                    mealId?.let {
                        viewModel.onEvent(RecipeDetailEvent.GetRecipeDetail(it))
                    }
                }
                RecipeDetailScreen(
                    viewModel = viewModel,
                    navHostController = navHostController,
                    onNavigationBackClick = {
                        viewModel.onEvent(RecipeDetailEvent.GoToRecipeListScreen)
                    },
                    onFavouriteClick = {
                        viewModel.onEvent(RecipeDetailEvent.InsertRecipe(it))
                    },
                    onDelete = {
                        viewModel.onEvent(RecipeDetailEvent.DeleteRecipe(it))
                    }
                )
            }
            composable(route = NavigationRoute.FavouriteScreen.route) {
                val viewModel = hiltViewModel<FavouriteViewModel>()
                FavouriteScreen(
                    viewModel = viewModel,
                    navHostController = navHostController
                ) { mealId ->
                    viewModel.onEvent(FavouriteEvent.GoToDetails(mealId))
                }
            }
        }
    }
}