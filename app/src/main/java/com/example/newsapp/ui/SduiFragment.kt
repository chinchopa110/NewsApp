package com.example.newsapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newsapp.data.repository.RemoteSduiRepository
import com.example.newsapp.databinding.FragmentSduiBinding
import com.example.newsapp.domain.model.Action
import com.example.newsapp.ui.sdui.SduiActionHandler
import com.example.newsapp.ui.sdui.SduiComponentFactory
import kotlinx.coroutines.launch

class SduiFragment : Fragment() {

    private var _binding: FragmentSduiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SduiViewModel by viewModels {
        SduiViewModel.Factory(RemoteSduiRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSduiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSdui.title = getString(com.example.newsapp.R.string.title_sdui)
        binding.btnRetry.setOnClickListener { viewModel.loadScreen() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: SduiScreenState) {
        binding.progressBar.isVisible = state is SduiScreenState.Loading
        binding.contentScroll.isVisible = state is SduiScreenState.Success
        binding.errorGroup.isVisible = state is SduiScreenState.Error

        when (state) {
            is SduiScreenState.Loading -> Unit
            is SduiScreenState.Error -> {
                binding.tvError.text = state.message
            }
            is SduiScreenState.Success -> {
                binding.toolbarSdui.title = state.screen.title
                renderComponents(state.screen)
            }
        }
    }

    private fun renderComponents(screen: com.example.newsapp.domain.model.SduiScreen) {
        binding.contentContainer.removeAllViews()
        val factory = SduiComponentFactory(requireContext(), createActionHandler())
        screen.components.forEach { component ->
            val view = factory.create(component)
            if (view.layoutParams == null) {
                view.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            binding.contentContainer.addView(view)
        }
    }

    private fun createActionHandler(): SduiActionHandler {
        return SduiActionHandler { action ->
            when (action) {
                is Action.OpenUrl -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(action.url)))
                }
                is Action.ShowToast -> {
                    Toast.makeText(requireContext(), action.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
