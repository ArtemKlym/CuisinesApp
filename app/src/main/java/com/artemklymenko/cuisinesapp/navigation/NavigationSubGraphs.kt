package com.artemklymenko.cuisinesapp.navigation

import com.artemklymenko.media_player.navigation.MediaPlayerFeatureApi
import com.artemklymenko.search.ui.navigation.SearchFeatureApi

data class NavigationSubGraphs(
    val searchFeatureApi: SearchFeatureApi,
    val mediaPlayerApi: MediaPlayerFeatureApi
)
