package com.example.userposts.data.di

import com.example.userposts.data.repository.FavoritesRepositoryImpl
import com.example.userposts.data.repository.UserPostsRepositoryImpl
import com.example.userposts.domain.repository.FavoritesRepository
import com.example.userposts.domain.repository.UserPostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(
        usersPostsRepositoryImpl: UserPostsRepositoryImpl
    ): UserPostsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}