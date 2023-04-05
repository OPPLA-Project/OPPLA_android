package com.umc.oppla.view.main.question.doanswer

import androidx.fragment.app.Fragment
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentDoanswerBinding
import com.umc.oppla.view.main.question.completeanswer.CompleteanswerFragment
import com.umc.oppla.view.main.question.questiondetail.QuestionDetailFragment


class DoanswerFragment : BaseFragment<FragmentDoanswerBinding>(R.layout.fragment_doanswer) {
    override fun init() {
        binding.doanswerTextviewSendanswer.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.questionblank_layout, CompleteanswerFragment(), "completeanswer")
                .commitAllowingStateLoss()
        }
    }
}