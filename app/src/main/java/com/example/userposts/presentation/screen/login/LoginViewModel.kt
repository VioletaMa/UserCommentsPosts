package com.example.userposts.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userposts.domain.model.User
import com.example.userposts.domain.usecase.LoginUserUseCase
import com.example.userposts.presentation.screen.posts.PostsListViewModel.ViewEffect
import com.example.userposts.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    data class ViewState(
        val isLoading: Boolean = false,
        val userId: Int? = null,
        val user: User? = null
    ) {
        val isLoginActive = userId != null
    }

    sealed class ViewEffect {
        data class ShowError(val message: String?) : ViewEffect()
        data class NavigateToPost(val userId: Int) : ViewEffect()
    }

    sealed class ViewEvent {
        object LoginPressed : ViewEvent()
        data class OnUserIdChanged(val userId: String) : ViewEvent()
    }

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableStateFlow<ViewEffect?>(null)
    val viewEffect = _viewEffect.asStateFlow()

    fun eventHandled() {
        _viewEffect.update { null }
    }

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.LoginPressed -> {
                _viewState.value.userId?.let { userId ->
                    loginUserUseCase.invoke(userId).onEach { result ->
                        when (result) {
                            is ResultWrapper.Error -> {
                                _viewState.update { it.copy(isLoading = false) }
                                _viewEffect.update { ViewEffect.ShowError(result.message) }
                            }

                            is ResultWrapper.Loading -> {
                                _viewState.update { it.copy(isLoading = true) }
                            }

                            is ResultWrapper.Success -> {
                                result.data?.let { user ->
                                    _viewState.update {
                                        it.copy(
                                            isLoading = false,
                                            user = user
                                        )
                                    }
                                    _viewEffect.update { ViewEffect.NavigateToPost(user.id) }
                                }
                            }
                        }
                    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
                }
            }

            is ViewEvent.OnUserIdChanged -> {
                _viewState.update { it.copy(userId = event.userId.toIntOrNull()) }
            }
        }
    }
}