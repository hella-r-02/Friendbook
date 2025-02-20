package com.src.book.domain.usecase.book

import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class GetAllTagsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var bookRepository: BookRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var getAllTagsUseCase: GetAllTagsUseCase

    @Before
    fun setUp() {
        bookRepository = mockk()
        getAllTagsUseCase = GetAllTagsUseCase(bookRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetAllTagsUseCaseSuccessful() = runTest {
        val tagsModel = listOf(testModelsGenerator.generateTagModel())
        coEvery { bookRepository.getAllTags() } returns BasicState.SuccessState(tagsModel)
        Assert.assertTrue(getAllTagsUseCase.execute() is BasicState.SuccessState)
        Assert.assertEquals(
            tagsModel,
            (getAllTagsUseCase.execute() as BasicState.SuccessState).data
        )
    }

    @Test
    fun testGetAllTagsUseCaseError() = runTest {
        coEvery { bookRepository.getAllTags() } returns BasicState.ErrorState()
        Assert.assertTrue(getAllTagsUseCase.execute() is BasicState.ErrorState)
    }
}