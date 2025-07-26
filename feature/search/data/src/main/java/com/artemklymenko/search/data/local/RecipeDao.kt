package com.artemklymenko.search.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.artemklymenko.search.domain.model.RecipeDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeDomain: RecipeDomain)

    @Delete
    suspend fun deleteRecipe(recipeDomain: RecipeDomain)

    @Update
    suspend fun updateRecipe(recipeDomain: RecipeDomain)

    @Query("SELECT * FROM RecipeDomain")
    fun getAllRecipes(): Flow<List<RecipeDomain>>
}