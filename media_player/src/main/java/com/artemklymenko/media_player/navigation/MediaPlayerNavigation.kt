package com.artemklymenko.media_player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.artemklymenko.common.navigation.FeatureApi
import com.artemklymenko.common.navigation.NavigationRoute
import com.artemklymenko.common.navigation.NavigationSubGraphRoute
import com.artemklymenko.media_player.screens.MediaPlayerScreen

interface MediaPlayerFeatureApi: FeatureApi

class MediaPlayerNavigationImpl: MediaPlayerFeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.MediaPlayer.route,
            startDestination = NavigationRoute.MediaPlayer.route,
        ) {
            composable(route = NavigationRoute.MediaPlayer.route) {
                val mediaPlayerVideoId = it.arguments?.getString("video_id")
                mediaPlayerVideoId?.let { id ->
                    MediaPlayerScreen(videoId = id)
                }
            }
        }
    }
}