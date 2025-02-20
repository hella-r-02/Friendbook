package com.src.book.domain.usecase.friend

import com.src.book.domain.model.Friend
import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFriendsUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute(): BasicState<List<Friend>> = withContext(Dispatchers.IO) {
        return@withContext friendRepository.getFriends()
    }
}