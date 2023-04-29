package com.umc.oppla.view.main.answer.answerlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.oppla.data.AnswerListDataTemp
import com.umc.oppla.databinding.ItemAnswerlistBinding

class AnswerListAdapter(clicklistener: onItemAnswerListClickInterface) :
    ListAdapter<AnswerListDataTemp, AnswerListAdapter.MyViewHolder>(AnswerListDiffUtil) {

    interface onItemAnswerListClickInterface {
        fun onItemTitleClick(id: Int, position: Int)
        fun onItemQuestionAgainClick(id: Int, position: Int)
        fun onItemAnswererCountClick(id: Int, position: Int)
    }

    var clicklistener: onItemAnswerListClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemAnswerlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(answer: AnswerListDataTemp) {
            binding.itemAnswerlistTextviewTitle.text = answer.title
            binding.itemAnswerlistTextviewAnswerercount.text = "답변자 ${answer.answerer}명"
            if (answer.state == 1) {
                binding.itemAnswerlistTextviewQuestionstate.text = "진행중"
                binding.itemAnswerlistTextviewQuestionstate.setTextColor(Color.parseColor("#D85050"))
                binding.itemAnswerlistTextviewQuestionagain.setTextColor(Color.parseColor("#80858D"))
            } else {
                binding.itemAnswerlistTextviewQuestionstate.text = "질문 종료"
                binding.itemAnswerlistTextviewQuestionstate.setTextColor(Color.parseColor("#80858D"))
                binding.itemAnswerlistTextviewQuestionagain.setTextColor(Color.parseColor("#4E8FFF"))
                binding.itemAnswerlistTextviewQuestionagain.setOnClickListener {
                    clicklistener.onItemQuestionAgainClick(answer.num, absoluteAdapterPosition)
                }
            }

            binding.itemAnswerlistTextviewTitle.setOnClickListener {
                clicklistener.onItemTitleClick(answer.num, absoluteAdapterPosition)
            }

            if (answer.answerer > 0) {
                binding.itemAnswerlistTextviewAnswerercount.setOnClickListener {
                    clicklistener.onItemAnswererCountClick(answer.num, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAnswerlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object AnswerListDiffUtil : DiffUtil.ItemCallback<AnswerListDataTemp>() {
    override fun areItemsTheSame(
        oldItem: AnswerListDataTemp,
        newItem: AnswerListDataTemp
    ): Boolean {
        return oldItem.num == newItem.num
    }

    override fun areContentsTheSame(
        oldItem: AnswerListDataTemp,
        newItem: AnswerListDataTemp
    ): Boolean {
        return oldItem == newItem
    }
}