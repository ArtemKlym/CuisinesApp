package com.artemklymenko.cuisinesapp.di

import com.artemklymenko.cuisinesapp.navigation.NavigationSubGraphs
import com.artemklymenko.media_player.navigation.MediaPlayerFeatureApi
import com.artemklymenko.search.ui.navigation.SearchFeatureApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNavigationSubGraph(
        searchFeatureApi: SearchFeatureApi,
        mediaPlayerFeatureApi: MediaPlayerFeatureApi
    ): NavigationSubGraphs {
        return NavigationSubGraphs(searchFeatureApi, mediaPlayerFeatureApi)
    }
}