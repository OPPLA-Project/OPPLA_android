package com.umc.oppla.view.main.home.search.result

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.oppla.data.model.Document
import com.umc.oppla.databinding.ItemSearchresultlistBinding

class SearchResultAdapter(clicklistener: onItemSearchResultClickInterface) :
    ListAdapter<Document, SearchResultAdapter.MyViewHolder>(QuestionListDiffUtil) {

    interface onItemSearchResultClickInterface {
        fun onItemSearchResultClick(lat: Double, lng: Double, name : String,address:String, position: Int)
    }

    var clicklistener: onItemSearchResultClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemSearchresultlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Document) {
            binding.itemSearchresultlistTextviewPlacename.text = result.place_name
            binding.itemSearchresultlistTextviewPlaceaddress.text = result.address_name
            binding.itemSearchresultlistLayout.setOnClickListener {
                clicklistener.onItemSearchResultClick(
                    result.y!!.toDouble(),
                    result.x!!.toDouble(),
                    result.place_name.toString(),
                    result.address_name.toString(),
                    absoluteAdapterPosition
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchresultlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object QuestionListDiffUtil : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.place_name == newItem.place_name
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}