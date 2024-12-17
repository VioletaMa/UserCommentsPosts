package com.example.userposts.data.response

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("postId") val postId: Int,
    @SerializedName("id") val commentId: Int,
    @SerializedName("name") val title: String,
    @SerializedName("email") val email: String,
    @SerializedName("body") val description: String
)