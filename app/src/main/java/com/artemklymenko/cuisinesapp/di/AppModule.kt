package com.artemklymenko.cuisinesapp.di

import android.content.Context
import com.artemklymenko.cuisinesapp.local.AppDatabase
import com.artemklymenko.cuisinesapp.navigation.NavigationSubGraphs
import com.artemklymenko.media_player.navigation.MediaPlayerFeatureApi
import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.ui.navigation.SearchFeatureApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.getRecipeDao()
    }
}