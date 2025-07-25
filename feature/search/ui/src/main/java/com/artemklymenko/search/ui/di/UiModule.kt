package com.artemklymenko.search.ui.di

import com.artemklymenko.search.ui.navigation.SearchFeatureApi
import com.artemklymenko.search.ui.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    fun provideSearchFeatureApi(): SearchFeatureApi {
        return SearchFeatureApiImpl()
    }
}