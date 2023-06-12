package com.umc.oppla.view.main.question.checkanswer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentCheckanswerBinding

class CheckanswerFragment : BaseFragment<FragmentCheckanswerBinding>(R.layout.fragment_checkanswer) {
    override fun init() {
        binding.apply {
            initAppbar(checkanswerToolbar.toolbarToolbar, null, true, null)
            checkanswerToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            checkanswerToolbar.toolbarTextviewTitle.text = "답변 확인하기"
        }

    }

}