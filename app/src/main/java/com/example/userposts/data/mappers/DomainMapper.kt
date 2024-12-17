package com.example.userposts.data.mappers

import com.example.userposts.data.response.CommentDto
import com.example.userposts.data.response.PostDto
import com.example.userposts.data.response.UserDto
import com.example.userposts.domain.model.Comment
import com.example.userposts.domain.model.Post
import com.example.userposts.domain.model.User

fun PostDto.toDomain() = Post(
    userId = userId,
    postId = postId,
    title = title,
    description = description,
    isFavorite = false
)

fun CommentDto.toDomain() = Comment(
    commentId = commentId,
    postId = postId,
    title = title,
    description = description,
    email = email
)

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    emailAddress = emailAddress
)