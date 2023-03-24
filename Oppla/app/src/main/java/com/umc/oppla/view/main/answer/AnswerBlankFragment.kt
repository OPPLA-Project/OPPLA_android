package com.umc.oppla.view.main.answer

import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentAnswerBlankBinding
import com.umc.oppla.view.main.answer.answerlist.AnswerListFragment

class AnswerBlankFragment : BaseFragment<FragmentAnswerBlankBinding>(R.layout.fragment_answer_blank) {
    override fun savedatainit() {
        // 초기 화면
        childFragmentManager
            .beginTransaction()
            .replace(R.id.answerblank_layout, AnswerListFragment(), "answerlist")
            .commitAllowingStateLoss()
    }
    override fun init() {

    }
}