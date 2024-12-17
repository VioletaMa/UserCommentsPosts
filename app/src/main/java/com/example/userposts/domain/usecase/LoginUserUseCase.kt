package com.example.userposts.domain.usecase

import com.example.userposts.domain.model.User
import com.example.userposts.domain.repository.UserPostsRepository
import com.example.userposts.util.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: UserPostsRepository
) {
    operator fun invoke(userId: Int): Flow<ResultWrapper<User>> = flow {
        emit(ResultWrapper.Loading())
        val result = repository.loginUser(userId)
        emit(result)
    }
}