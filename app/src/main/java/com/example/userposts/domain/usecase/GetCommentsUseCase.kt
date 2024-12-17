package com.example.userposts.domain.usecase

import com.example.userposts.domain.model.Comment
import com.example.userposts.domain.repository.UserPostsRepository
import com.example.userposts.util.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: UserPostsRepository
) {
    operator fun invoke(postId: Int): Flow<ResultWrapper<List<Comment>>> = flow {
        emit(ResultWrapper.Loading())
        val result = repository.fetchAllComments(postId)
        emit(result)
    }
}