package com.example.newsapp.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import coil.load
import com.example.newsapp.databinding.ViewArticleCardBinding
import com.example.newsapp.domain.model.Article
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ArticleCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    val binding: ViewArticleCardBinding = ViewArticleCardBinding.inflate(
        LayoutInflater.from(context), this
    )

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    init {
        radius = dpToPx(16f)
        cardElevation = dpToPx(2f)
        setContentPadding(0, 0, 0, 0)
        
        strokeWidth = dpToPx(1f).toInt()
        setStrokeColor(android.content.res.ColorStateList.valueOf(0x14000000))
    }

    fun setArticle(article: Article) {
        binding.ivArticleImage.transitionName = "image_${article.url}"
        binding.tvTitle.transitionName = "title_${article.url}"
        binding.tvSource.transitionName = "source_${article.url}"
        binding.tvDescription.transitionName = "desc_${article.url}"
        this.transitionName = "card_${article.url}"

        binding.tvTitle.text = article.title
        binding.tvSource.text = article.source.name
        binding.tvDescription.text = article.description
        binding.tvAuthor.text = article.author?.let { "By $it" } ?: "Unknown Author"

        val dateStr = try {
            val date = inputFormat.parse(article.publishedAt)
            date?.let { outputFormat.format(it) } ?: article.publishedAt
        } catch (e: Exception) {
            article.publishedAt
        }
        binding.tvPublishedAt.text = dateStr

        if (!article.urlToImage.isNullOrEmpty()) {
            binding.ivArticleImage.visibility = View.VISIBLE
            binding.ivArticleImage.load(article.urlToImage) {
                crossfade(false)
            }
        } else {
            binding.ivArticleImage.visibility = View.GONE
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}
