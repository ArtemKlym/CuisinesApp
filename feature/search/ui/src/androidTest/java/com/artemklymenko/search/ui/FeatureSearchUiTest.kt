package com.artemklymenko.search.ui

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artemklymenko.common.navigation.NavigationRoute
import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.search.domain.use_cases.local.DeleteRecipeUseCase
import com.artemklymenko.search.domain.use_cases.local.GetAllRecipesFromLocalDbUseCase
import com.artemklymenko.search.domain.use_cases.local.InsertRecipeUseCase
import com.artemklymenko.search.domain.use_cases.remote.GetAllRecipeUseCase
import com.artemklymenko.search.domain.use_cases.remote.GetRecipeDetailUseCase
import com.artemklymenko.search.ui.repository.FakeFailureRepositoryImpl
import com.artemklymenko.search.ui.repository.FakeSuccessRepositoryImpl
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteEvent
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteScreen
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteScreenTestTag
import com.artemklymenko.search.ui.screens.favoutire_recipes.FavouriteViewModel
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailEvent
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailScreen
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailScreenTestTag
import com.artemklymenko.search.ui.screens.recipe_details.RecipeDetailViewModel
import com.artemklymenko.search.ui.screens.recipe_list.RecipeEvent
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListScreen
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListScreenTag
import com.artemklymenko.search.ui.screens.recipe_list.RecipeListViewModel
import com.artemklymenko.search.ui.utils.getRecipeResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeatureSearchUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var getAllRecipeUseCase: GetAllRecipeUseCase
    private lateinit var getAllRecipesFromLocalDbUseCase: GetAllRecipesFromLocalDbUseCase
    private lateinit var getRecipeDetailUseCase: GetRecipeDetailUseCase
    private lateinit var insertRecipeUseCase: InsertRecipeUseCase
    private lateinit var deleteRecipeUseCase: DeleteRecipeUseCase

    private lateinit var fakeSuccessRepository: FakeSuccessRepositoryImpl
    private lateinit var fakeFailureRepository: FakeFailureRepositoryImpl

    companion object {
        const val QUERY_STRING = "chicken"
    }

    @Before
    fun setup() {
        fakeSuccessRepository = FakeSuccessRepositoryImpl()
        fakeFailureRepository = FakeFailureRepositoryImpl()
    }

    @After
    fun tearDown() {
        fakeSuccessRepository.reset()
        fakeFailureRepository.reset()
    }

    private fun initSuccessUseCases() {
        getAllRecipeUseCase = GetAllRecipeUseCase(fakeSuccessRepository)
        getRecipeDetailUseCase = GetRecipeDetailUseCase(fakeSuccessRepository)
        getAllRecipesFromLocalDbUseCase = GetAllRecipesFromLocalDbUseCase(fakeSuccessRepository)
        insertRecipeUseCase = InsertRecipeUseCase(fakeSuccessRepository)
        deleteRecipeUseCase = DeleteRecipeUseCase(fakeSuccessRepository)
    }

    private fun initFailureUseCases() {
        getAllRecipeUseCase = GetAllRecipeUseCase(fakeFailureRepository)
        getRecipeDetailUseCase = GetRecipeDetailUseCase(fakeFailureRepository)
        getAllRecipesFromLocalDbUseCase = GetAllRecipesFromLocalDbUseCase(fakeFailureRepository)
        insertRecipeUseCase = InsertRecipeUseCase(fakeFailureRepository)
        deleteRecipeUseCase = DeleteRecipeUseCase(fakeFailureRepository)
    }

    private fun testingEnvironment() {
        val recipeListViewModel = RecipeListViewModel(getAllRecipeUseCase)
        val recipeDetailViewModel =
            RecipeDetailViewModel(getRecipeDetailUseCase, deleteRecipeUseCase, insertRecipeUseCase)
        val favouriteViewModel =
            FavouriteViewModel(getAllRecipesFromLocalDbUseCase, deleteRecipeUseCase)

        composeRule.setContent {
            val navHostController = rememberNavController()
            NavHost(
                navController = navHostController,
                startDestination = NavigationRoute.RecipeList.route
            ) {
                composable(route = NavigationRoute.RecipeList.route) {
                    RecipeListScreen(
                        viewModel = recipeListViewModel,
                        navHostController = navHostController,
                        onDetailClick = { mealId ->
                            recipeListViewModel.onEvent(RecipeEvent.GoToRecipeDetails(id = mealId))
                        }
                    )
                }

                composable(route = NavigationRoute.RecipeDetails.route) { mealArgumentId ->
                    val mealId = mealArgumentId.arguments?.getString("id")
                    LaunchedEffect(mealId) {
                        mealId?.let {
                            recipeDetailViewModel.onEvent(RecipeDetailEvent.GetRecipeDetail(it))
                        }
                    }
                    RecipeDetailScreen(
                        viewModel = recipeDetailViewModel,
                        navHostController = navHostController,
                        onNavigationBackClick = {
                            recipeDetailViewModel.onEvent(RecipeDetailEvent.GoToRecipeListScreen)
                        },
                        onFavouriteClick = {
                            recipeDetailViewModel.onEvent(RecipeDetailEvent.InsertRecipe(it))
                        },
                        onDelete = {
                            recipeDetailViewModel.onEvent(RecipeDetailEvent.DeleteRecipe(it))
                        }
                    )
                }
                composable(route = NavigationRoute.FavouriteScreen.route) {
                    FavouriteScreen(
                        viewModel = favouriteViewModel,
                        navHostController = navHostController
                    ) { mealId ->
                        favouriteViewModel.onEvent(FavouriteEvent.GoToDetails(mealId))
                    }
                }
            }
        }
    }

    @Test
    fun test_recipeListSuccess() {
        initSuccessUseCases()
        testingEnvironment()
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(timeoutMillis = 5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).assertIsDisplayed()
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal.plus(0)))
        }
    }

    @Test
    fun test_recipeListFailure() {
        initFailureUseCases()
        testingEnvironment()
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(timeoutMillis = 5000) {
                onAllNodesWithText(NetworkError.NO_INTERNET.toString()).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithText(NetworkError.NO_INTERNET.toString()).assertIsDisplayed()
        }
    }

    @Test
    fun test_recipeListSuccess_recipeDetailSuccess() {
        initSuccessUseCases()
        testingEnvironment()
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(timeoutMillis = 5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).assertIsDisplayed()
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal.plus(0)))
            onNodeWithTag(getRecipeResponse().first().strMeal.plus(0)).performClick()
            waitForIdle()
            onNodeWithText(getRecipeResponse().first().strMeal).assertIsDisplayed()
        }
    }

    @Test
    fun test_insertion() {
        initSuccessUseCases()
        testingEnvironment()
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(timeoutMillis = 5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).assertIsDisplayed()
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal.plus(0)))
            onNodeWithTag(getRecipeResponse().first().strMeal.plus(0)).performClick()

            onNodeWithTag(RecipeDetailScreenTestTag.INSERT).performClick()
            onNodeWithTag(RecipeDetailScreenTestTag.ARROW_BACK).performClick()

            onNodeWithTag(RecipeListScreenTag.FLOATING_ACTION_BTN).performClick()
            waitForIdle()
            onNodeWithText(getRecipeResponse().first().strMeal).assertIsDisplayed()
        }
    }

    @Test
    fun test_deletion() {
        initSuccessUseCases()
        testingEnvironment()
        with(composeRule) {
            onNodeWithTag(RecipeListScreenTag.SEARCH).performClick()
            onNodeWithTag(RecipeListScreenTag.SEARCH).performTextInput(QUERY_STRING)
            waitUntil(timeoutMillis = 5000) {
                onAllNodesWithTag(RecipeListScreenTag.LAZY_COL).fetchSemanticsNodes().isNotEmpty()
            }
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).assertIsDisplayed()
            onNodeWithTag(RecipeListScreenTag.LAZY_COL).onChildAt(0)
                .assert(hasTestTag(getRecipeResponse().first().strMeal.plus(0)))
            onNodeWithTag(getRecipeResponse().first().strMeal.plus(0)).performClick()

            onNodeWithTag(RecipeDetailScreenTestTag.INSERT).performClick()
            onNodeWithTag(RecipeDetailScreenTestTag.ARROW_BACK).performClick()

            onNodeWithTag(RecipeListScreenTag.FLOATING_ACTION_BTN).performClick()
            waitForIdle()
            onNodeWithText(getRecipeResponse().first().strMeal).assertIsDisplayed()

            onNodeWithTag(FavouriteScreenTestTag.DELETE).performClick()
            onNodeWithText("Nothing found").assertIsDisplayed()
        }
    }
}