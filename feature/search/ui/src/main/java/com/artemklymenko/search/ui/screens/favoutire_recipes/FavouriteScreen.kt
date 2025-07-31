package com.artemklymenko.search.ui.screens.favoutire_recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.artemklymenko.common.navigation.NavigationRoute
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.ui.R
import com.artemklymenko.search.ui.components.ExpandableText
import kotlinx.coroutines.flow.collectLatest

object FavouriteScreenTestTag {
    const val LAZY_COL = "lazy_col"
    const val DROP_DOWN = "drop_down"
    const val ALPHABETICAL = "alphabetical"
    const val LESS_INGREDIENT = "less_ingredient"
    const val RESET = "reset"
    const val DELETE = "delete"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    viewModel: FavouriteViewModel,
    navHostController: NavHostController,
    onDetailClick: (String) -> Unit
) {
    val showDropDown = rememberSaveable {
        mutableStateOf(false)
    }
    val selectedIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { navigation ->
                when (navigation) {
                    is FavouriteNavigationEvent.GoToRecipeDetailScreen -> {
                        navHostController.navigate(NavigationRoute.RecipeDetails.sendId(navigation.id))
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favourite_recipes),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.testTag(FavouriteScreenTestTag.DROP_DOWN),
                        onClick = {
                        showDropDown.value = showDropDown.value.not()
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                        if (showDropDown.value) {
                            DropdownMenu(
                                expanded = showDropDown.value,
                                onDismissRequest = {
                                    showDropDown.value = !showDropDown.value
                                }
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier.testTag(FavouriteScreenTestTag.ALPHABETICAL),
                                    text = {
                                        Text(
                                            text = stringResource(R.string.alphabetical)
                                        )
                                    },
                                    onClick = {
                                        selectedIndex.value = 0
                                        showDropDown.value = showDropDown.value
                                        viewModel.onEvent(FavouriteEvent.AlphabeticalSort)
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedIndex.value == 0,
                                            onClick = {
                                                selectedIndex.value = 0
                                                showDropDown.value = showDropDown.value
                                                viewModel.onEvent(FavouriteEvent.AlphabeticalSort)
                                            }
                                        )
                                    })
                                DropdownMenuItem(
                                    modifier = Modifier.testTag(FavouriteScreenTestTag.LESS_INGREDIENT),
                                    text = {
                                        Text(
                                            text = stringResource(R.string.less_ingredients)
                                        )
                                    },
                                    onClick = {
                                        selectedIndex.value = 1
                                        showDropDown.value = showDropDown.value
                                        viewModel.onEvent(FavouriteEvent.LessIngredientSort)
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedIndex.value == 1,
                                            onClick = {
                                                selectedIndex.value = 1
                                                showDropDown.value = showDropDown.value
                                                viewModel.onEvent(FavouriteEvent.LessIngredientSort)
                                            }
                                        )
                                    })
                                DropdownMenuItem(
                                    modifier = Modifier.testTag(FavouriteScreenTestTag.RESET),
                                    text = {
                                        Text(
                                            text = stringResource(R.string.reset)
                                        )
                                    },
                                    onClick = {
                                        selectedIndex.value = 2
                                        showDropDown.value = showDropDown.value
                                        viewModel.onEvent(FavouriteEvent.ResetSort)
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedIndex.value == 2,
                                            onClick = {
                                                selectedIndex.value = 2
                                                showDropDown.value = showDropDown.value
                                                viewModel.onEvent(FavouriteEvent.ResetSort)
                                            }
                                        )
                                    })
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.value.errorMessage.let { error ->
            if (error !is UiText.Idle) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error.getString(context))
                }
            }
        }
        uiState.value.data?.let { favouriteRecipes ->
            if (favouriteRecipes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.nothing_found))
                }
            } else {
                FavouriteScreenContent(
                    favouriteRecipes = favouriteRecipes,
                    modifier = Modifier.padding(innerPadding),
                    onDetailClick = onDetailClick,
                    onDeleteClick = { recipe ->
                        viewModel.onEvent(FavouriteEvent.DeleteRecipe(recipe))
                    }
                )
            }
        }
    }
}

@Composable
private fun FavouriteScreenContent(
    favouriteRecipes: List<RecipeDomain>,
    modifier: Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (RecipeDomain) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
            .testTag(FavouriteScreenTestTag.LAZY_COL)
    ) {
        itemsIndexed(favouriteRecipes) { index, recipe ->
            FavouriteRecipeContent(
                recipe = recipe,
                index = index,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun FavouriteRecipeContent(
    recipe: RecipeDomain,
    index: Int,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (RecipeDomain) -> Unit
) {
    Card(
        modifier = Modifier
            .testTag("FavouriteItem_${recipe.idMeal + index}")
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onDetailClick(recipe.idMeal) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = recipe.strMealThumb,
                contentDescription = recipe.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { onDeleteClick(recipe) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .testTag(FavouriteScreenTestTag.DELETE)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = recipe.strMeal,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            ExpandableText(
                text = recipe.strInstructions,
                showMoreText = stringResource(R.string.show_more),
                showLessText = stringResource(R.string.show_less),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (recipe.strTags.isNotEmpty()) {
                FlowRow {
                    recipe.strTags.split(",")
                        .forEach {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .wrapContentSize()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        width = 1.dp,
                                        color = Color.Red,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
