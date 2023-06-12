package com.umc.oppla.view.main.question.questiondetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentQuestionDetailBinding
import com.umc.oppla.databinding.FragmentQuestionListBinding

import com.umc.oppla.view.main.question.doanswer.DoanswerFragment

class QuestionDetailFragment :
    BaseFragment<FragmentQuestionDetailBinding>(R.layout.fragment_question_detail) {
    override fun init() {
        binding.apply {
            initAppbar(questiondetailToolbar.toolbarToolbar, null, true, null)
            questiondetailToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            questiondetailToolbar.toolbarTextviewTitle.text = "질문 상세 보기"
        }

        binding.qustiondetailTextviewDoanswer.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.questionblank_layout, DoanswerFragment(), "doanswer")
                .addToBackStack("questiondetail")
                .commitAllowingStateLoss()
        }
    }
}