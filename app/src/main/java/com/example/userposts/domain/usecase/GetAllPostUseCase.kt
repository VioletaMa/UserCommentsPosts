package com.example.userposts.domain.usecase

import com.example.userposts.domain.model.Post
import com.example.userposts.domain.repository.UserPostsRepository
import com.example.userposts.util.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllPostUseCase @Inject constructor(
    private val repository: UserPostsRepository
) {
    operator fun invoke(userId: Int): Flow<ResultWrapper<List<Post>>> = flow {
        emit(ResultWrapper.Loading())
        val result = repository.fetchAllPosts(userId)
        emit(result)
    }
}