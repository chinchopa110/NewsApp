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
import androidx.transition.TransitionInflater
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
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
        
        // Настройка Transition Names
        binding.ivDetailImage.transitionName = "image_${article.url}"
        binding.tvDetailTitle.transitionName = "title_${article.url}"
        binding.tvDetailSource.transitionName = "source_${article.url}"
        binding.tvDetailDescription.transitionName = "desc_${article.url}"

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

        // Анимация появления контента. Описание ТЕПЕРЬ ОСТАЕТСЯ.
        animateContentTransition()
    }

    private fun animateContentTransition() {
        val viewsToFadeIn = listOf(
            binding.tvDetailContent,
            binding.tvReadMoreLabel,
            binding.tvDetailUrl,
            binding.btnDetailBlockAuthor,
            binding.btnDetailBlockSource,
            binding.tvDetailAuthorAndDate
        )

        viewsToFadeIn.forEach { view ->
            view.animate()
                .alpha(1f)
                .setDuration(600)
                .setStartDelay(200)
                .start()
        }
    }

    private fun setupUI(article: Article) {
        binding.tvDetailTitle.text = article.title
        binding.tvDetailSource.text = article.source.name
        binding.tvDetailDescription.text = article.description
        
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
            binding.ivDetailImage.load(article.urlToImage) {
                crossfade(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
