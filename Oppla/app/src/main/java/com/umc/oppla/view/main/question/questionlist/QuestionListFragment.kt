package com.umc.oppla.view.main.question.questionlist

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.QuestionListDataTemp
import com.umc.oppla.databinding.FragmentQuestionListBinding
import com.umc.oppla.view.main.question.questiondetail.QuestionDetailFragment

class QuestionListFragment :
    BaseFragment<FragmentQuestionListBinding>(R.layout.fragment_question_list) {
    private lateinit var questionlistadapter: QuestionListAdapter
    private lateinit var notificationAdapter: NotificationAdapter

    override fun init() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            initAppbar(questionlistToolbar.toolbarToolbar, null, false, null)
            questionlistToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            questionlistToolbar.toolbarTextviewTitle.text = "내가 받은 질문 목록"
        }

        questionlistadapter =
            QuestionListAdapter(object : QuestionListAdapter.onItemQuestionDetailClickInterface {
                override fun onItemQuestionDetailClick(id: Int, position: Int) {
                    // 초기 화면
                    parentFragmentManager
                        .beginTransaction()
                        .add(R.id.questionblank_layout, QuestionDetailFragment(), "questiondetail")
                        .addToBackStack("questionlist")
                        .commitAllowingStateLoss()
                }
            })

        notificationAdapter = NotificationAdapter()
        val concatAdapter = ConcatAdapter(questionlistadapter, notificationAdapter)

        binding.qustionlistRecyclerviewQuestions.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = concatAdapter
        }

        val data = mutableListOf<QuestionListDataTemp>()
        data.add(QuestionListDataTemp(1, 10, "제목1", "주소~~~"))
        data.add(QuestionListDataTemp(2, 1, "제목2", "주소~~~"))
        data.add(QuestionListDataTemp(3, 2, "제목3", "주소~~~"))
        data.add(QuestionListDataTemp(4, 3, "제목4", "주소~~~"))
        data.add(QuestionListDataTemp(5, 4, "제목5~~~", "주소~~~"))
        data.add(QuestionListDataTemp(6, 5, "제목6~~~", "주소~~~"))
        data.add(QuestionListDataTemp(7, 6, "제목7~~~", "주소~~~"))

        questionlistadapter.submitList(data)
        notificationAdapter.textview[0] = true
    }

}