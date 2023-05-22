package com.umc.oppla.view.main.question.doanswer

import android.view.View
import androidx.fragment.app.Fragment
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentDoanswerBinding
import com.umc.oppla.view.main.question.completeanswer.CompleteanswerFragment
import com.umc.oppla.view.main.question.questiondetail.QuestionDetailFragment


class DoanswerFragment : BaseFragment<FragmentDoanswerBinding>(R.layout.fragment_doanswer) {
    override fun init() {
        binding.apply {
            initAppbar(doanswerToolbar.toolbarToolbar, null, true, null)
            doanswerToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            doanswerToolbar.toolbarTextviewTitle.text = "답변하기"
        }

        binding.doanswerTextviewSendanswer.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.questionblank_layout, CompleteanswerFragment(), "completeanswer")
                .commitAllowingStateLoss()
        }
    }
}