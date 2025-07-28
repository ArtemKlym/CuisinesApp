package com.artemklymenko.media_player.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun MediaPlayerScreen(
    videoId: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentSecond by rememberSaveable { mutableStateOf(0f) }
    AndroidView(modifier = Modifier.fillMaxSize(), factory = {
        YouTubePlayerView(it).apply {
            lifecycleOwner.lifecycle.addObserver(this)
            addYouTubePlayerListener(object: YouTubePlayerListener {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, currentSecond)
                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    currentSecond = second
                }

                override fun onApiChange(youTubePlayer: YouTubePlayer) {}
                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
                override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
                override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
                override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
                override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
            })
        }
    })
}