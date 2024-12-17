package com.example.userposts.presentation.screen.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userposts.R
import com.example.userposts.databinding.FragmentCommentsBinding
import com.example.userposts.presentation.screen.posts.PostsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private val viewModel by viewModels<CommentsViewModel>()
    private lateinit var binding: FragmentCommentsBinding

    private val commentsAdapter = CommentsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentCommentsBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommentsBinding.bind(view)

        binding.commentRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = commentsAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.viewState.collect(::viewStateListener) }
                launch { viewModel.eventState.collect(::viewEffectListener) }
            }
        }
    }

    private fun viewStateListener(viewState: CommentsViewModel.ViewState) {
        commentsAdapter.submitList(viewState.comments)
        binding.progressBarLayout.isVisible = viewState.isLoading
        binding.mainContainer.isVisible = !viewState.isLoading
        viewState.post?.let { post ->
            binding.layoutPost.txtPostTitle.text = post.title
            binding.layoutPost.txtPostDescription.text = post.description

            binding.layoutPost.btnFavorite.apply {
                setBackgroundColor(
                    if (post.isFavorite) ContextCompat.getColor(
                        context,
                        R.color.blue_active
                    ) else ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
                setTextColor(
                    if (post.isFavorite) ContextCompat.getColor(
                        context,
                        R.color.white
                    ) else ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
            }

            binding.layoutPost.btnFavorite.setOnClickListener {
                viewModel.onEvent(CommentsViewModel.ViewEvent.FavoritePressed(post))
            }
        }
    }

    private fun viewEffectListener(viewEffect: CommentsViewModel.ViewEffect?) {
        when (viewEffect) {
            is CommentsViewModel.ViewEffect.ShowError -> viewEffect.message?.let {
                Toast.makeText(requireContext(), viewEffect.message, Toast.LENGTH_LONG).show()
            }

            null -> Unit
        }
        viewModel.eventHandled()
    }
}