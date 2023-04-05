package com.umc.oppla.view.main.question.questionlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.oppla.R
import com.umc.oppla.databinding.ItemNotificationBinding

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.TodoViewHolder>() {

    val textview = mutableListOf<Boolean>(false)

    inner class TodoViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notification, viewGroup, false)
        return TodoViewHolder(ItemNotificationBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int) {
        if (textview[0]) {
            viewHolder.binding.itemNotificationLayout.visibility = View.VISIBLE
        } else {
            viewHolder.binding.itemNotificationLayout.visibility = View.GONE
        }
    }

    override fun getItemCount() = textview.size
}