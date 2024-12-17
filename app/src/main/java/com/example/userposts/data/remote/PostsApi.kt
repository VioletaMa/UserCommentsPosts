package com.example.userposts.data.remote

import com.example.userposts.data.response.CommentDto
import com.example.userposts.data.response.PostDto
import com.example.userposts.data.response.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApi {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") userId: Int
    ): UserDto

    @GET("users/{id}/posts")
    suspend fun getPosts(
        @Path("id") userId: Int
    ): List<PostDto>

    @GET("/posts/{id}/comments")
    suspend fun getComments(
        @Path("id") postId: Int
    ): List<CommentDto>
}