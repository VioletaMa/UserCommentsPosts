package com.example.userposts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val userId: Int,
    val postId: Int,
    val title: String,
    val description: String,
    val isFavorite: Boolean
) : Parcelable