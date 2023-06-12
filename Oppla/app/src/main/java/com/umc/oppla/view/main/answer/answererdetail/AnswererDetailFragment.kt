package com.umc.oppla.view.main.answer.answererdetail

import android.view.View
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentAnswererDetailBinding


class AnswererDetailFragment : BaseFragment<FragmentAnswererDetailBinding>(R.layout.fragment_answerer_detail) {
    override fun init() {
        binding.apply {
            initAppbar(answererdetailToolbar.toolbarToolbar, null, true, null)
            answererdetailToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            answererdetailToolbar.toolbarTextviewTitle.text = "답변자 상세보기"
        }
    }

}