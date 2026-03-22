package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.newsapp.data.repository.InMemoryUserRepository
import com.example.newsapp.databinding.FragmentArticleDetailBinding
import com.example.newsapp.domain.model.Article
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleDetailFragmentArgs by navArgs()

    private val blacklistViewModel: BlacklistViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BlacklistViewModel(InMemoryUserRepository.getInstance(requireContext())) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article
        setupUI(article)

        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnDetailBlockAuthor.setOnClickListener {
            article.author?.let { author ->
                blacklistViewModel.blockAuthor(author)
                findNavController().popBackStack()
            }
        }

        binding.btnDetailBlockSource.setOnClickListener {
            if (article.source.id != null) {
                blacklistViewModel.blockSourceId(article.source.id)
            } else {
                blacklistViewModel.blockSourceName(article.source.name)
            }
            findNavController().popBackStack()
        }
    }

    private fun setupUI(article: Article) {
        binding.tvDetailTitle.text = article.title
        binding.tvDetailSource.text = article.source.name
        
        // Remove [+2050 chars] pattern from content
        val cleanedContent = article.content.replace(Regex("\\[\\+\\d+\\schar[s]?\\]"), "").trim()
        binding.tvDetailContent.text = cleanedContent

        binding.tvDetailUrl.text = article.url

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        val dateStr = try {
            val date = inputFormat.parse(article.publishedAt)
            date?.let { outputFormat.format(it) } ?: article.publishedAt
        } catch (e: Exception) {
            article.publishedAt
        }

        binding.tvDetailAuthorAndDate.text = article.author?.let { "By $it • $dateStr" } ?: dateStr

        if (!article.urlToImage.isNullOrEmpty()) {
            binding.ivDetailImage.visibility = View.VISIBLE
            binding.ivDetailImage.load(article.urlToImage) {
                crossfade(true)
            }
        } else {
            binding.ivDetailImage.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
