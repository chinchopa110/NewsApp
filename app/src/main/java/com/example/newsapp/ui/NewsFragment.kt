package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.repository.NetworkArticleRepository
import com.example.newsapp.data.repository.InMemoryUserRepository
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.components.ArticleCardView
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewsViewModel(
                    NetworkArticleRepository("021cf7a054f24277aa5149a5eda6bae7"),
                    InMemoryUserRepository.getInstance(requireContext())
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        binding.recyclerViewNews.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        updateThemeIcon()

        binding.btnToggleTheme.setOnClickListener {
            val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        val adapter = NewsAdapter(
            onArticleClick = { article ->
                val layoutManager = binding.recyclerViewNews.layoutManager as LinearLayoutManager
                val position = (binding.recyclerViewNews.adapter as NewsAdapter).currentList.indexOf(article)
                val viewHolder = binding.recyclerViewNews.findViewHolderForAdapterPosition(position)
                
                if (viewHolder != null) {
                    val cardView = viewHolder.itemView.findViewById<ArticleCardView>(R.id.articleCard)
                    val extras = FragmentNavigatorExtras(
                        cardView.binding.ivArticleImage to "image_${article.url}",
                        cardView.binding.tvTitle to "title_${article.url}",
                        cardView.binding.tvSource to "source_${article.url}",
                        cardView.binding.tvDescription to "desc_${article.url}"
                    )
                    
                    val action = NewsFragmentDirections.actionNewsFragmentToArticleDetailFragment(article)
                    findNavController().navigate(action, extras)
                } else {
                    val action = NewsFragmentDirections.actionNewsFragmentToArticleDetailFragment(article)
                    findNavController().navigate(action)
                }
            }
        )
        binding.recyclerViewNews.adapter = adapter

        binding.recyclerViewNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.loadNextPage()
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.articles.collect { articles ->
                        adapter.submitList(articles)
                    }
                }
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.swipeRefreshLayout.isRefreshing = isLoading
                    }
                }
            }
        }
    }

    private fun updateThemeIcon() {
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (isDarkMode) {
            binding.btnToggleTheme.setImageResource(R.drawable.ic_sun)
        } else {
            binding.btnToggleTheme.setImageResource(R.drawable.ic_moon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
