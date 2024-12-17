package com.example.userposts.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface FavoritesRepository {

    val favoritesPosts: StateFlow<List<Int>>

    fun addItem(item: Int)

    fun removeItem(item: Int)
}