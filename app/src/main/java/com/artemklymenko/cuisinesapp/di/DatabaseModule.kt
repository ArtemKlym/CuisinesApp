package com.artemklymenko.cuisinesapp.di

import android.content.Context
import com.artemklymenko.cuisinesapp.local.AppDatabase
import com.artemklymenko.search.data.local.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.getRecipeDao()
    }
}