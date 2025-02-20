package com.src.book.data.remote.model.book.bookList

import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.domain.model.book.BookList

class BookListMapper(
    private val authorBookMapper: AuthorBookMapper,
    private val genreMapper: GenreMapper
) {
    suspend fun mapFromResponseToModel(data: BookListResponse, isAuth: Boolean): BookList {
        return BookList(
            id = data.id,
            name = data.name,
            rating = data.rating,
            linkCover = data.linkCover,
            year = data.year,
            authors = data.authors.map { authorBookMapper.mapFromResponseToModel(it) },
            isWantToRead = data.isWantToRead,
            grade = data.grade,
            genres = data.genres.map { genreMapper.mapFromResponseToModel(it) },
            isAuth = isAuth
        )
    }
}