package com.artemklymenko.search.domain.use_cases.local

import com.artemklymenko.search.domain.model.RecipeDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DeleteRecipeUseCaseTest {

    private lateinit var searchRepository: FakeSearchRepository
    private lateinit var deleteRecipeUseCase: DeleteRecipeUseCase

    @Before
    fun setup() {
        searchRepository = FakeSearchRepository()
        deleteRecipeUseCase = DeleteRecipeUseCase(searchRepository)
    }

    @Test
    fun `delete a recipe from database`() = runTest{
        deleteRecipeUseCase.invoke(getRecipeResponse()).collect(collector = {
            val list = searchRepository.getAllRecipes().first()
            assertEquals(emptyList<RecipeDomain>(), list)
        })
    }
}