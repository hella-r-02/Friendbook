package com.src.book.domain.repository

import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.repository.BookRepositoryImpl
import com.src.book.domain.model.book.BookList
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class BookRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var bookDataSource: BookDataSource
    private lateinit var bookRepository: BookRepository

    @Before
    fun setUp() {
        bookRepository = BookRepositoryImpl(bookDataSource)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetBookByIdSuccessful() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookDataSource.loadBookById(any()) } returns bookModel
        Assert.assertEquals(
            bookModel,
            bookRepository.getBookById(1)
        )
    }

    @Test
    fun testGetBooksByAuthorByIdSuccessful() = runTest {
        val booksModel = testModelsGenerator.generateListOfBookListModel()
        val state = BasicState.SuccessState(booksModel)
        coEvery { bookDataSource.loadBooksByAuthorId(any()) } returns state
        Assert.assertTrue(
            bookRepository.getBooksByAuthorId(ID) is BasicState.SuccessState<*>
        )
        Assert.assertEquals(
            (bookRepository.getBooksByAuthorId(ID) as BasicState.SuccessState<*>).data,
            booksModel
        )
    }

    @Test
    fun testGetBooksByAuthorByIdEmptyList() = runTest {
        val state = BasicState.EmptyState<List<BookList>>()
        coEvery { bookDataSource.loadBooksByAuthorId(any()) } returns state
        Assert.assertTrue(
            bookRepository.getBooksByAuthorId(ID) is BasicState.EmptyState
        )
    }

    @Test
    fun testGetBooksByAuthorByIdError() = runTest {
        coEvery { bookDataSource.loadBooksByAuthorId(any()) } returns BasicState.ErrorState()
        Assert.assertTrue(
            bookRepository.getBooksByAuthorId(ID) is BasicState.ErrorState
        )
    }

    @Test
    fun testGetAllGenresSuccessful() = runTest {
        val genresModel = listOf(testModelsGenerator.generateGenreModel())
        coEvery { bookDataSource.loadAllGenres() } returns BasicState.SuccessState(genresModel)
        Assert.assertTrue(bookRepository.getAllGenres() is BasicState.SuccessState)
        Assert.assertEquals(
            genresModel,
            (bookRepository.getAllGenres() as BasicState.SuccessState).data
        )
    }

    @Test
    fun testGetAllGenresError() = runTest {
        coEvery { bookDataSource.loadAllGenres() } returns BasicState.ErrorState()
        Assert.assertTrue(bookRepository.getAllGenres() is BasicState.ErrorState)
    }

    @Test
    fun testGetAllTagsSuccessful() = runTest {
        val tagsModel = listOf(testModelsGenerator.generateTagModel())
        coEvery { bookDataSource.loadAllTags() } returns BasicState.SuccessState(tagsModel)
        Assert.assertTrue(bookRepository.getAllTags() is BasicState.SuccessState)
        Assert.assertEquals(
            tagsModel,
            (bookRepository.getAllTags() as BasicState.SuccessState).data
        )
    }

    @Test
    fun testGetAllTagsError() = runTest {
        coEvery { bookDataSource.loadAllTags() } returns BasicState.ErrorState()
        Assert.assertTrue(bookRepository.getAllTags() is BasicState.ErrorState)
    }

    @Test
    fun testRemoveBookmarkSuccessful() = runTest {
        coEvery { bookDataSource.removeBookmark(any()) } returns BookmarkState.SuccessState
        Assert.assertTrue(bookDataSource.removeBookmark(ID) is BookmarkState.SuccessState)
    }

    @Test
    fun testRemoveBookmarkError() = runTest {
        coEvery { bookDataSource.removeBookmark(any()) } returns BookmarkState.ErrorState
        Assert.assertTrue(bookDataSource.removeBookmark(ID) is BookmarkState.ErrorState)
    }

    @Test
    fun testRemoveBookmarkNOtAuthorizedError() = runTest {
        coEvery { bookDataSource.addBookmark(any()) } returns BookmarkState.NotAuthorizedState
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.NotAuthorizedState)
    }

    @Test
    fun testAddBookmarkSuccessful() = runTest {
        coEvery { bookDataSource.addBookmark(any()) } returns BookmarkState.SuccessState
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.SuccessState)
    }

    @Test
    fun testAddBookmarkError() = runTest {
        coEvery { bookDataSource.addBookmark(any()) } returns BookmarkState.ErrorState
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.ErrorState)
    }

    @Test
    fun testAddBookmarkNOtAuthorizedError() = runTest {
        coEvery { bookDataSource.addBookmark(any()) } returns BookmarkState.NotAuthorizedState
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.NotAuthorizedState)
    }
}