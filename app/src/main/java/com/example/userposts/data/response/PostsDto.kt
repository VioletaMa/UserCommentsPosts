package com.example.userposts.data.response

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val description: String
)