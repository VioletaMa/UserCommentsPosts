package com.example.userposts.domain.model

data class Comment(
    val postId: Int,
    val commentId: Int,
    val title: String,
    val email: String,
    val description: String
)