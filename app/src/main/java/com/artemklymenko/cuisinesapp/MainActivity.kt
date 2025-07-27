package com.artemklymenko.cuisinesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.artemklymenko.cuisinesapp.navigation.NavigationSubGraphs
import com.artemklymenko.cuisinesapp.navigation.RecipeNavigation
import com.artemklymenko.cuisinesapp.ui.theme.CuisinesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationSubGraphs: NavigationSubGraphs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuisinesAppTheme {
                Surface(modifier = Modifier.safeContentPadding()) {
                    RecipeNavigation(
                        navigationSubGraphs = navigationSubGraphs
                    )
                }
            }
        }
    }
}