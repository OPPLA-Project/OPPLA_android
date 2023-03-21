package com.umc.oppla.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.umc.oppla.R
import com.umc.oppla.base.BaseActivity
import com.umc.oppla.databinding.ActivityMainBinding
import com.umc.oppla.view.main.answer.AnswerFragment
import com.umc.oppla.view.main.home.HomeBlankFragment
import com.umc.oppla.view.main.mypage.MypageFragment
import com.umc.oppla.view.main.question.QuestionFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var currentFragmenttag: String

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmenttag = savedInstanceState.get("homeblank").toString()
    }

    override fun savedatainit() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.mainFragmentContainerViewContainer.id, HomeBlankFragment(), "homeblank")
            .commitAllowingStateLoss()
        currentFragmenttag = "homeblank" // 현재 보고 있는 fragmet의 Tag
    }

    override fun init() {

        // 네비게이션 버튼 클릭시 프래그먼트 전환
        binding.mainBottomnavigationBnb.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeBaseFragment -> { // 첫 번째 fragment
                    changeFragment("homeblank", HomeBlankFragment())
                }
                R.id.favoriteBaseFragment -> { // 두 번째 fragment
                    changeFragment("question", QuestionFragment())
                }
                R.id.locationFragment -> { // 세 번째 fragment
                    changeFragment("answer", AnswerFragment())
                }
                R.id.chatFragment -> { // 세 번째 fragment
                    changeFragment("mypage", MypageFragment())
                }
            }
            true
        }
    }

    private fun changeFragment(tag: String, fragment: Fragment) {
        // supportFragmentManager에 "first"라는 Tag로 저장된 fragment 있는지 확인
        if (supportFragmentManager.findFragmentByTag(tag) == null) { // Tag가 없을 때 -> 없을 리가 없다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .add(binding.mainFragmentContainerViewContainer.id, fragment, tag)
                .commitAllowingStateLoss()

        } else { // Tag가 있을 때
            // 먼저 currentFragmenttag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commitAllowingStateLoss()
        }
        // currentFragmenttag에 '현재 fragment Tag' "first"를 저장한다.
        currentFragmenttag = tag
    }

}