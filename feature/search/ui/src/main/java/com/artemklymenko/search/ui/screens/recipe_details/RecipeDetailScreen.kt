package com.artemklymenko.search.ui.screens.recipe_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.artemklymenko.search.domain.model.RecipeDetailsDomain
import com.artemklymenko.search.ui.R
import com.artemklymenko.search.ui.components.ExpandableText
import kotlinx.coroutines.flow.collectLatest

object RecipeDetailScreenTestTag {
    const val INSERT = "insert"
    const val DELETE = "delete"
    const val ARROW_BACK = "arrow_back"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel,
    navHostController: NavHostController,
    onNavigationBackClick: () -> Unit,
    onDelete: (RecipeDetailsDomain) -> Unit,
    onFavouriteClick: (RecipeDetailsDomain) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { navigation ->
                when (navigation) {
                    RecipeDetailNavigationEvent.GoToNavigationList -> {
                        navHostController.popBackStack()
                    }

                    is RecipeDetailNavigationEvent.GoToMediaPlayer -> {
                        val videoId = navigation.youtubeUrl.split("v=").last()
                        navHostController.navigate(NavigationRoute.MediaPlayer.sendUrl(videoId))
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.value.data?.strMeal ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onNavigationBackClick()
                        }
                            .testTag(RecipeDetailScreenTestTag.ARROW_BACK)
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            uiState.value.data?.let {
                                onFavouriteClick(it)
                            }
                        },
                        modifier = Modifier.testTag(RecipeDetailScreenTestTag.INSERT)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            uiState.value.data?.let {
                                onDelete(it)
                            }
                        },
                        modifier = Modifier.testTag(RecipeDetailScreenTestTag.DELETE)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                })
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
        uiState.value.data?.let { recipeDetail ->
            RecipeDetailContent(
                recipeDetail = recipeDetail,
                modifier = Modifier.padding(innerPadding),
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun RecipeDetailContent(
    recipeDetail: RecipeDetailsDomain,
    modifier: Modifier,
    onEvent: (RecipeDetailEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = recipeDetail.strMealThumb,
            contentDescription = recipeDetail.strMeal,
            modifier = Modifier.size(320.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))
        ExpandableText(
            text = recipeDetail.strInstructions,
            showMoreText = stringResource(R.string.show_more),
            showLessText = stringResource(R.string.show_less),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        )
        Spacer(modifier = Modifier.height(12.dp))
        recipeDetail.ingredientsPair.forEach {
            if (it.first.isNotEmpty() || it.second.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = getIngredientsImageUrl(it.first),
                        contentDescription = it.first,
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    )
                    Text(
                        text = it.second,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (recipeDetail.strYoutube.isNotEmpty()) {
            Text(
                text = "Watch YouTube Video",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable {
                    onEvent(RecipeDetailEvent.GoToMediaPlayer(youtubeUrl = recipeDetail.strYoutube))
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun getIngredientsImageUrl(name: String) =
    "https://www.themealdb.com/images/ingredients/${name}.png"
