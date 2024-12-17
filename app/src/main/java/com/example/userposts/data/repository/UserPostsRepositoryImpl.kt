package com.example.userposts.data.repository

import com.example.userposts.data.mappers.toDomain
import com.example.userposts.data.remote.PostsApi
import com.example.userposts.domain.model.Comment
import com.example.userposts.domain.model.Post
import com.example.userposts.domain.model.User
import com.example.userposts.domain.repository.UserPostsRepository
import com.example.userposts.util.ResultWrapper
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserPostsRepositoryImpl @Inject constructor(
    private val postsApi: PostsApi,
) : UserPostsRepository {

    override suspend fun fetchAllPosts(userId: Int): ResultWrapper<List<Post>> {
        return try {
            val remoteListings = postsApi.getPosts(userId)
            ResultWrapper.Success(
                data = remoteListings.map { it.toDomain() }
            )
        } catch (e: IOException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        }
    }

    override suspend fun fetchAllComments(postId: Int): ResultWrapper<List<Comment>> {
        return try {
            val remoteListings = postsApi.getComments(postId)
            ResultWrapper.Success(
                data = remoteListings.map { it.toDomain() }
            )
        } catch (e: IOException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        }
    }

    override suspend fun loginUser(userId: Int): ResultWrapper<User> {
        return try {
            val remoteListings = postsApi.getUser(userId)
            ResultWrapper.Success(
                data = remoteListings.toDomain()
            )
        } catch (e: IOException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            ResultWrapper.Error("Couldn't load data")
        }
    }

}