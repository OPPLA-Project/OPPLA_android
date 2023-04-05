package com.umc.oppla.view.main.question.questionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.oppla.data.QuestionListDataTemp
import com.umc.oppla.databinding.ItemQuestionlistBinding

class QuestionListAdapter(clicklistener: onItemQuestionDetailClickInterface) :
    ListAdapter<QuestionListDataTemp, QuestionListAdapter.MyViewHolder>(QuestionListDiffUtil) {

    interface onItemQuestionDetailClickInterface {
        fun onItemQuestionDetailClick(id:Int, position: Int)
    }

    var clicklistener: onItemQuestionDetailClickInterface = clicklistener

    inner class MyViewHolder(val binding:ItemQuestionlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuestionListDataTemp) {
            binding.itemQuestionlistTextviewAddress.text = question.address
            binding.itemQuestionlistTextviewTitle.text = question.title
            binding.itemQuestionlistTextviewRemaintime.text = question.remainTime.toString()
            binding.itemQuestionlistTextviewDetail.setOnClickListener {
                clicklistener.onItemQuestionDetailClick(question.num, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemQuestionlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object QuestionListDiffUtil : DiffUtil.ItemCallback<QuestionListDataTemp>() {
    override fun areItemsTheSame(oldItem: QuestionListDataTemp, newItem: QuestionListDataTemp): Boolean {
        return oldItem.num == newItem.num
    }

    override fun areContentsTheSame(oldItem: QuestionListDataTemp, newItem: QuestionListDataTemp): Boolean {
        return oldItem == newItem
    }
}