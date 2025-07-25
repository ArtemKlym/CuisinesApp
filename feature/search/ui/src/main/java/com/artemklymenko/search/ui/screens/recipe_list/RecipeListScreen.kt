package com.artemklymenko.search.ui.screens.recipe_list

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.artemklymenko.common.navigation.NavigationRoute
import com.artemklymenko.common.utils.ConnectivityObserverImpl
import com.artemklymenko.common.utils.NetworkStatus
import com.artemklymenko.common.utils.UiText
import com.artemklymenko.search.domain.model.RecipeDomain
import com.artemklymenko.search.ui.R
import com.artemklymenko.search.ui.components.ExpandableText
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    navHostController: NavHostController,
    onDetailClick: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val connectivityObserver = remember { ConnectivityObserverImpl(context) }
    val networkStatus by connectivityObserver.networkStatus.collectAsState(initial = NetworkStatus.AVAILABLE)

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when (it) {
                is RecipeNavigationEvent.GoToRecipeDetails -> {
                    navHostController.navigate(NavigationRoute.RecipeDetails.sendId(it.id))
                }
                is RecipeNavigationEvent.GoToFavoriteScreen -> {
                    navHostController.navigate((NavigationRoute.FavouriteScreen.route))
                }
            }
        }
    }

    LaunchedEffect(networkStatus) {
        if (networkStatus == NetworkStatus.AVAILABLE) {
            viewModel.onEvent(RecipeEvent.NetworkRestored)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(RecipeEvent.GoToFavouriteScreen)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        },
        topBar = {
            TextField(
                value = query, onValueChange = {
                    query = it
                    if (query.isNotBlank()) {
                        viewModel.onEvent(RecipeEvent.SearchRecipe(query))
                    }
                }, colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if(query.isBlank()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.lets_find_a_recipe),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        uiState.value.errorMessage.let { error ->
            if (error !is UiText.Idle) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error.getString(context))
                }
            }
        }
        uiState.value.data?.let { recipeList ->
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(recipeList) { recipe ->
                    RecipeContent(
                        recipe = recipe,
                        onDetailClick = onDetailClick
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeContent(
    recipe: RecipeDomain,
    onDetailClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onDetailClick(recipe.idMeal) },
        shape = RoundedCornerShape(16.dp)
    ) {
        AsyncImage(
            model = recipe.strMealThumb,
            contentDescription = recipe.strMeal,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

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