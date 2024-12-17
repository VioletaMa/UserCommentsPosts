package com.example.userposts.data.repository

import com.example.userposts.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor() : FavoritesRepository {

    private val _mutableList = MutableStateFlow<List<Int>>(emptyList())

    override val favoritesPosts: StateFlow<List<Int>>
        get() = _mutableList

    override fun addItem(item: Int) {
        val updatedList = _mutableList.value.toMutableList().apply { add(item) }
        _mutableList.value = updatedList
    }

    override fun removeItem(item: Int) {
        val updatedList = _mutableList.value.toMutableList().apply { remove(item) }
        _mutableList.value = updatedList
    }
}