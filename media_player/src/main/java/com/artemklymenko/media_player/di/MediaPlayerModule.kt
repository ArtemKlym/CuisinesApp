package com.artemklymenko.media_player.di

import com.artemklymenko.media_player.navigation.MediaPlayerFeatureApi
import com.artemklymenko.media_player.navigation.MediaPlayerNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MediaPlayerModule {

    @Provides
    fun provideMediaPlayerFeatureApi(): MediaPlayerFeatureApi {
        return MediaPlayerNavigationImpl()
    }
}