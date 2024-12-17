package com.example.userposts.presentation.screen.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userposts.R
import com.example.userposts.databinding.FragmentPostsListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private val viewModel by viewModels<PostsListViewModel>()
    private lateinit var binding: FragmentPostsListBinding

    companion object {
        const val POST = "post"
    }

    private val postsAdapter = PostsListAdapter(onItemClick = { post ->
        findNavController().navigate(R.id.commentsFragment, bundleOf(POST to post))
    },
        onFavoriteClick = { post ->
            viewModel.onEvent(PostsListViewModel.ViewEvent.FavoritePressed(post))
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentPostsListBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostsListBinding.bind(view)

        binding.postRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postsAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.viewState.collect(::viewStateListener) }
                launch { viewModel.viewEffect.collect(::viewEffectListener) }
            }
        }

        binding.btnFilterAll.setOnClickListener {
            viewModel.onEvent(PostsListViewModel.ViewEvent.OnFilterAllPressed)
        }
        binding.btnFilterFavorites.setOnClickListener {
            viewModel.onEvent(PostsListViewModel.ViewEvent.OnFilterFavoritesPressed)
        }
    }

    private fun viewStateListener(viewState: PostsListViewModel.ViewState) {
        postsAdapter.submitList(viewState.loadedPosts)
        binding.progressBarLayout.isVisible = viewState.isLoading
    }

    private fun viewEffectListener(viewEffect: PostsListViewModel.ViewEffect?) {
        when (viewEffect) {
            is PostsListViewModel.ViewEffect.ShowError -> viewEffect.message?.let {
                Toast.makeText(requireContext(), viewEffect.message, Toast.LENGTH_LONG).show()
            }

            null -> Unit
        }
        viewModel.eventHandled()
    }
}