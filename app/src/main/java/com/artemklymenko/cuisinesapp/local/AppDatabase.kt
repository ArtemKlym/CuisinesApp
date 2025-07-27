package com.artemklymenko.cuisinesapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artemklymenko.search.data.local.RecipeDao
import com.artemklymenko.search.domain.model.RecipeDomain

@Database(
    entities = [RecipeDomain::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "recipe_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun getRecipeDao(): RecipeDao
}