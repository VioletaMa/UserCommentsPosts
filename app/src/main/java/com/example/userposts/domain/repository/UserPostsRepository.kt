package com.example.userposts.domain.repository

import com.example.userposts.domain.model.Comment
import com.example.userposts.domain.model.Post
import com.example.userposts.domain.model.User
import com.example.userposts.util.ResultWrapper

interface UserPostsRepository {

    suspend fun fetchAllPosts(userId: Int): ResultWrapper<List<Post>>

    suspend fun fetchAllComments(postId: Int): ResultWrapper<List<Comment>>

    suspend fun loginUser(userId: Int): ResultWrapper<User>
}