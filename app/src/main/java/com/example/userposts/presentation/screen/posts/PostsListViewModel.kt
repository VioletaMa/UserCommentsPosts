package com.example.userposts.presentation.screen.posts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userposts.domain.model.Post
import com.example.userposts.domain.repository.FavoritesRepository
import com.example.userposts.domain.usecase.GetAllPostUseCase
import com.example.userposts.presentation.screen.login.LoginFragment.Companion.USER_ID
import com.example.userposts.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val getAllPostUseCase: GetAllPostUseCase,
    savedStateHandle: SavedStateHandle,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    data class ViewState(
        val isLoading: Boolean = false,
        val allPostsList: List<Post> = emptyList(),
        val loadedPosts: List<Post> = emptyList()
    )

    sealed class ViewEffect {
        data class ShowError(val message: String?) : ViewEffect()
    }

    sealed class ViewEvent {
        data class FavoritePressed(val post: Post) : ViewEvent()
        object OnFilterAllPressed : ViewEvent()
        object OnFilterFavoritesPressed : ViewEvent()
    }

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableStateFlow<ViewEffect?>(null)
    val viewEffect = _viewEffect.asStateFlow()

    fun eventHandled() {
        _viewEffect.update { null }
    }

    init {
        val userId: Int? = savedStateHandle[USER_ID]

        userId?.let {
            getAllPostUseCase.invoke(userId).combine(
                favoritesRepository.favoritesPosts
            ) { result, favorites ->
                when (result) {
                    is ResultWrapper.Error -> {
                        _viewState.update { it.copy(isLoading = false) }
                        _viewEffect.update { ViewEffect.ShowError(result.message) }
                    }

                    is ResultWrapper.Loading -> {
                        _viewState.update { it.copy(isLoading = true) }
                    }

                    is ResultWrapper.Success -> {
                        result.data?.let { posts ->
                            _viewState.update { state ->
                                val postsList = posts.map {
                                    it.copy(
                                        isFavorite = favorites.contains(
                                            it.postId
                                        )
                                    )
                                }
                                state.copy(
                                    isLoading = false,
                                    allPostsList = postsList,
                                    loadedPosts = postsList
                                )
                            }
                        }
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
        }
    }

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.FavoritePressed -> {
                if (event.post.isFavorite) {
                    favoritesRepository.removeItem(event.post.postId)
                } else {
                    favoritesRepository.addItem(event.post.postId)
                }
            }

            ViewEvent.OnFilterFavoritesPressed -> {
                _viewState.update { state -> state.copy(loadedPosts = state.allPostsList.filter { it.isFavorite }) }
            }

            ViewEvent.OnFilterAllPressed -> {
                _viewState.update { it.copy(loadedPosts = it.allPostsList) }
            }
        }
    }
}