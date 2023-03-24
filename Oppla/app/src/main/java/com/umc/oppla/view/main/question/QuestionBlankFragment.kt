package com.umc.oppla.view.main.question

import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentQuestionBlankBinding
import com.umc.oppla.view.main.question.questionlist.QuestionListFragment

class QuestionBlankFragment : BaseFragment<FragmentQuestionBlankBinding>(R.layout.fragment_question_blank) {
    override fun savedatainit() {
        // 초기 화면
        childFragmentManager
            .beginTransaction()
            .replace(R.id.questionblank_layout, QuestionListFragment(), "questionlist")
            .commitAllowingStateLoss()
    }
    override fun init() {

    }
}