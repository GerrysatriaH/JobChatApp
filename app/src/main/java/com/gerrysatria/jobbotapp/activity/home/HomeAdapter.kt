package com.gerrysatria.jobbotapp.activity.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerrysatria.jobbotapp.data.response.HistoryChatResponse
import com.gerrysatria.jobbotapp.databinding.ItemHistoryChatBinding
import com.gerrysatria.jobbotapp.utils.formatDate

class HomeAdapter(
    private val onItemClick: (HistoryChatResponse) -> Unit
) : ListAdapter<HistoryChatResponse, HomeAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(private val binding: ItemHistoryChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryChatResponse) {
            binding.dateCreated.text = formatDate(data.createdAt)
            binding.root.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<HistoryChatResponse>() {
            override fun areItemsTheSame(oldItem: HistoryChatResponse, newItem: HistoryChatResponse): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: HistoryChatResponse, newItem: HistoryChatResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}