package com.example.userposts.presentation.screen.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userposts.domain.model.Comment
import com.example.userposts.domain.model.Post
import com.example.userposts.domain.repository.FavoritesRepository
import com.example.userposts.domain.usecase.GetCommentsUseCase
import com.example.userposts.presentation.screen.posts.PostsListFragment.Companion.POST
import com.example.userposts.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    sealed class ViewEffect {
        data class ShowError(val message: String?) : ViewEffect()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val post: Post? = null,
        val comments: List<Comment> = emptyList()
    )

    sealed class ViewEvent {
        data class FavoritePressed(val post: Post) : ViewEvent()
    }

    private val _viewState = MutableStateFlow(ViewState(isLoading = true))
    val viewState = _viewState.asStateFlow()

    private val _eventState = MutableStateFlow<ViewEffect?>(null)
    val eventState = _eventState.asStateFlow()

    fun eventHandled() {
        _eventState.update { null }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val receivedPost: Post? = savedStateHandle[POST]
            receivedPost?.let { post ->
                _viewState.update { it.copy(post = post) }
                getCommentsUseCase.invoke(post.postId).onEach { result ->
                    when (result) {
                        is ResultWrapper.Error -> {
                            _viewState.update { it.copy(isLoading = false) }
                            _eventState.update { ViewEffect.ShowError(result.message) }
                        }

                        is ResultWrapper.Loading -> _viewState.update { it.copy(isLoading = true) }
                        is ResultWrapper.Success -> {
                            val receivedComments = result.data
                            _viewState.update {
                                it.copy(
                                    isLoading = false,
                                    comments = receivedComments ?: emptyList()
                                )
                            }
                        }
                    }
                }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
            }
        }
    }

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.FavoritePressed -> {
                _viewState.update { it.copy(post = it.post?.copy(isFavorite = !event.post.isFavorite)) }
                if (event.post.isFavorite) {
                    favoritesRepository.removeItem(event.post.postId)
                } else {
                    favoritesRepository.addItem(event.post.postId)
                }
            }
        }
    }
}