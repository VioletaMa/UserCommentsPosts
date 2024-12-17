package com.example.userposts.presentation.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.userposts.R
import com.example.userposts.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding

    companion object {
        const val USER_ID = "userId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentLoginBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.viewState.collect(::viewStateListener) }
                launch { viewModel.viewEffect.collect(::viewEffectListener) }
            }
        }
    }

    private fun viewStateListener(viewState: LoginViewModel.ViewState) {
        with(binding) {
            btnLogin.isEnabled = viewState.isLoginActive

            btnLogin.setOnClickListener {
                viewModel.onEvent(LoginViewModel.ViewEvent.LoginPressed)
            }

            txtUserId.doAfterTextChanged {
                viewModel.onEvent(LoginViewModel.ViewEvent.OnUserIdChanged(it.toString()))
            }
        }
    }

    private fun viewEffectListener(viewEffect: LoginViewModel.ViewEffect?) {
        when (viewEffect) {
            is LoginViewModel.ViewEffect.ShowError -> Toast.makeText(
                requireContext(),
                viewEffect.message,
                Toast.LENGTH_LONG
            ).show()

            is LoginViewModel.ViewEffect.NavigateToPost -> findNavController().navigate(
                R.id.postsListFragment, bundleOf(
                    USER_ID to viewEffect.userId
                )
            )

            null -> Unit
        }
        viewModel.eventHandled()
    }
}