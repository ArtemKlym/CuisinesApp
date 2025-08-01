package com.artemklymenko.cuisinesapp.di

import android.content.Context
import androidx.room.Room
import com.artemklymenko.cuisinesapp.local.AppDatabase
import com.artemklymenko.cuisinesapp.repository.FakeSearchRepository
import com.artemklymenko.search.data.di.SearchDataModule
import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchDataModule::class, DatabaseModule::class]
)
object TestDi {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.getRecipeDao()
    }

    @Provides
    fun provideRepositoryImpl(recipeDao: RecipeDao): SearchRepository {
        return FakeSearchRepository(recipeDao)
    }
}