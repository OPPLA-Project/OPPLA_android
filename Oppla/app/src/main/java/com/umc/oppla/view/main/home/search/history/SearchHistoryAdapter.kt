package com.umc.oppla.view.main.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.oppla.databinding.ItemSearchhistoryBinding


class SearchHistoryAdapter(val clicklistener: SearchHistoryAdapter.ItemClickListener) :
    ListAdapter<String, SearchHistoryAdapter.MyViewHolder>(HomeNewDiffUtil) {

    interface ItemClickListener {
        fun onTextItemClick(searchhistory: String, position: Int)
        fun onImageItemClick(searchhistory: String, position: Int)
    }

    inner class MyViewHolder(val binding: ItemSearchhistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: String) {

            binding.searchhistoryTextviewHistory.text = history

            binding.searchhistoryLayoutDetail.setOnClickListener {
                clicklistener.onTextItemClick(history, absoluteAdapterPosition)
            }

            binding.searchhistoryImageviewDelete.setOnClickListener {
                clicklistener.onImageItemClick(history, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchhistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object HomeNewDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}