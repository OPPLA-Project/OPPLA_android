package com.umc.oppla.view.main.answer.answerlist

import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.AnswerListDataTemp
import com.umc.oppla.databinding.FragmentAnswerListBinding
import com.umc.oppla.view.main.answer.answererlist.AnswererListFragment
import com.umc.oppla.view.main.home.doquestion.DoquestionFragment

class AnswerListFragment : BaseFragment<FragmentAnswerListBinding>(R.layout.fragment_answer_list) {
    private lateinit var answerListAdapter: AnswerListAdapter

    override fun init() {
        initRecyclerView()
        binding.answerlistTextviewSort.setOnClickListener {

        }
    }
    private fun initRecyclerView() {
        answerListAdapter =
            AnswerListAdapter(object : AnswerListAdapter.onItemAnswerListClickInterface {
                override fun onItemTitleClick(id: Int, position: Int) {
                    parentFragmentManager
                        .beginTransaction()
                        .add(R.id.answerblank_layout, DoquestionFragment(), "doqeustion")
                        .addToBackStack("answerlist")
                        .commitAllowingStateLoss()
                }

                override fun onItemQuestionAgainClick(id: Int, position: Int) {
                    // 해당 데이터 상태 변경 api 호출
                }

                override fun onItemAnswererCountClick(id: Int, position: Int) {
                    parentFragmentManager
                        .beginTransaction()
                        .add(R.id.answerblank_layout, AnswererListFragment(), "answererlist")
                        .addToBackStack("answerlist")
                        .commitAllowingStateLoss()
                }

            })

        binding.answerlistRecyclerAnswerlist.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = answerListAdapter
        }

        val data = mutableListOf<AnswerListDataTemp>()
        data.add(AnswerListDataTemp(1, 1, "제목1", 2))
        data.add(AnswerListDataTemp(2, 1, "제목2", 0))
        data.add(AnswerListDataTemp(3, 0, "제목3", 1))
        data.add(AnswerListDataTemp(4, 0, "제목4", 5))
        data.add(AnswerListDataTemp(5, 1, "제목5~~~", 0))
        data.add(AnswerListDataTemp(6, 0, "제목6~~~", 0))
        data.add(AnswerListDataTemp(7, 1, "제목7~~~", 2))

        answerListAdapter.submitList(data)
    }
}