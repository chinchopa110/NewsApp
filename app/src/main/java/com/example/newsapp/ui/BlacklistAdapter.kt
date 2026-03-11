package com.example.newsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemBlacklistBinding

class BlacklistAdapter(
    private val onUnblock: (String) -> Unit
) : ListAdapter<String, BlacklistAdapter.BlacklistViewHolder>(DiffCallback) {

    class BlacklistViewHolder(private val binding: ItemBlacklistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: String, onUnblock: (String) -> Unit) {
            binding.tvValue.text = value
            binding.btnUnblock.setOnClickListener {
                onUnblock(value)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlacklistViewHolder {
        return BlacklistViewHolder(
            ItemBlacklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BlacklistViewHolder, position: Int) {
        holder.bind(getItem(position), onUnblock)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}