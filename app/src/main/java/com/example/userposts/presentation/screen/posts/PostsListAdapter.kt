package com.example.userposts.presentation.screen.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userposts.R
import com.example.userposts.domain.model.Post

class PostsListAdapter(
    private val onItemClick: (Post) -> Unit,
    private val onFavoriteClick: (Post) -> Unit
) : ListAdapter<Post, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(getItem(position), onItemClick, onFavoriteClick)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            post: Post,
            onItemClick: (Post) -> Unit,
            onFavoriteClick: (Post) -> Unit
        ) = with(itemView) {
            findViewById<TextView>(R.id.txt_post_title).text =
                post.title
            findViewById<TextView>(R.id.txt_post_description).text =
                post.description
            with(findViewById<Button>(R.id.btn_favorite)) {
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
            findViewById<Button>(R.id.btn_favorite).setOnClickListener {
                onFavoriteClick(post)
            }
            setOnClickListener { onItemClick(post) }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem.title == newItem.title && oldItem.description == newItem.description
        }
    }
}