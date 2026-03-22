package com.example.newsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.domain.model.Article
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsAdapter(
    private val onArticleClick: (Article) -> Unit
) : ListAdapter<Article, NewsAdapter.NewsViewHolder>(DiffCallback) {

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        
        private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        private val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        fun bind(article: Article, onArticleClick: (Article) -> Unit) {
            binding.tvTitle.text = article.title
            binding.tvAuthor.text = article.author?.let { "By $it" } ?: "Unknown Author"
            binding.tvSource.text = article.source.name
            binding.tvDescription.text = article.description
            
            try {
                val date = inputFormat.parse(article.publishedAt)
                binding.tvPublishedAt.text = date?.let { outputFormat.format(it) } ?: article.publishedAt
            } catch (e: Exception) {
                binding.tvPublishedAt.text = article.publishedAt
            }

            if (!article.urlToImage.isNullOrEmpty()) {
                binding.ivArticleImage.visibility = View.VISIBLE
                binding.ivArticleImage.load(article.urlToImage) {
                    crossfade(true)
                }
            } else {
                binding.ivArticleImage.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onArticleClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), onArticleClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }
}
