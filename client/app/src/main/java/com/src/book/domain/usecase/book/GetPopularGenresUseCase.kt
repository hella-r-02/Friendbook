package com.src.book.domain.usecase.book

import com.src.book.domain.model.Genre
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPopularGenresUseCase(private val bookRepository: BookRepository) {
    suspend fun execute():BasicState<List<Genre>> = withContext(Dispatchers.IO){
        return@withContext bookRepository.getPopularGenres()
    }
}