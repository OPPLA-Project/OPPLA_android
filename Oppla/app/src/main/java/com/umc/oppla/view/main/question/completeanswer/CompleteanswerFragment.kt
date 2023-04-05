package com.umc.oppla.view.main.question.completeanswer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentCompleteanswerBinding
import com.umc.oppla.view.main.question.questiondetail.QuestionDetailFragment

class CompleteanswerFragment : BaseFragment<FragmentCompleteanswerBinding>(R.layout.fragment_completeanswer) {
    override fun init() {
        binding.completeanswerTextviewComplete.setOnClickListener {
            parentFragmentManager
                .popBackStackImmediate()
        }
    }
}