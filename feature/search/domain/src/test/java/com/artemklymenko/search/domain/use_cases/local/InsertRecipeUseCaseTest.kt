package com.artemklymenko.search.domain.use_cases.local

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InsertRecipeUseCaseTest {

    private lateinit var searchRepository: FakeSearchRepository
    private lateinit var useCase: InsertRecipeUseCase

    @Before
    fun setup() {
        searchRepository = FakeSearchRepository()
        useCase = InsertRecipeUseCase(searchRepository)
    }

    @Test
    fun `insert a recipe to database`() = runTest {
        useCase.invoke(getRecipeResponse()).collect(collector = {
            val list = searchRepository.getAllRecipes().first()
            assertEquals(getRecipeResponse(),list.first())
        })
    }
}