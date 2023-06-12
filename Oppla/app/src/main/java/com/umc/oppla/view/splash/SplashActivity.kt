package com.umc.oppla.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.umc.oppla.R
import com.umc.oppla.base.BaseActivity
import com.umc.oppla.databinding.ActivitySplashBinding
import com.umc.oppla.view.login.LoginActivity
import com.umc.oppla.view.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun init() {
        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({
            // 토큰 존재 여부 확인하기
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                            //로그인 필요
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            //기타 에러
                            Log.e("whatisthis","splashactivity "+error.toString())
                            finish()
                        }
                    }
                    else {
                        //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                        // 사용자 이름 얻으려고
                        UserApiClient.instance.me { user, error ->
                            var username = user?.kakaoAccount?.profile?.nickname.toString()
                            Toast.makeText(this, "${username}님 환영합니다.", Toast.LENGTH_SHORT).show()
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            else {
                //로그인 필요
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 1000) // 시간 0.5초 이후 실행
    }
}