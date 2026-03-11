package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newsapp.data.repository.InMemoryUserRepository
import com.example.newsapp.databinding.FragmentBlacklistBinding
import kotlinx.coroutines.launch

class BlacklistFragment : Fragment() {

    private var _binding: FragmentBlacklistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BlacklistViewModel by viewModels {
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
        _binding = FragmentBlacklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authorsAdapter = BlacklistAdapter { author ->
            viewModel.unblockAuthor(author)
        }
        binding.rvBlockedAuthors.adapter = authorsAdapter

        val idsAdapter = BlacklistAdapter { id ->
            viewModel.unblockSourceId(id)
        }
        binding.rvBlockedSourceIds.adapter = idsAdapter

        val namesAdapter = BlacklistAdapter { name ->
            viewModel.unblockSourceName(name)
        }
        binding.rvBlockedSourceNames.adapter = namesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    authorsAdapter.submitList(user.blockedAuthors.toList())
                    idsAdapter.submitList(user.blockedSourceIds.toList())
                    namesAdapter.submitList(user.blockedSourceNames.toList())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}