package com.umc.oppla.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil.setContentView
import com.umc.oppla.R
import com.umc.oppla.base.BaseActivity
import com.umc.oppla.databinding.ActivitySplashBinding
import com.umc.oppla.view.login.LoginActivity
import com.umc.oppla.view.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun init() {
        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000) // 시간 0.5초 이후 실행
    }
}