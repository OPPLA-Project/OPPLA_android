package com.umc.oppla.view.login


import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.oppla.ApplicationClass
import com.umc.oppla.R
import com.umc.oppla.base.BaseActivity
import com.umc.oppla.data.remote.LoginService
import com.umc.oppla.data.model.auth.AuthRequest
import com.umc.oppla.databinding.ActivityLoginBinding
import com.umc.oppla.repository.LoginRepository
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.viewmodel.LoginViewModel
import com.umc.oppla.viewmodel.factory.LoginViewModelFactory
import com.umc.oppla.widget.utils.ApiClient
import com.umc.oppla.widget.utils.NetworkResult

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    lateinit var loginViewModel: LoginViewModel

    override fun init() {
        val loginService = ApiClient.retrofit(ApplicationClass().getDataStore()).create(LoginService::class.java)
        val loginRepository = LoginRepository(loginService, ApplicationClass().getDataStore())
        val loginViewModelFactory = LoginViewModelFactory(loginRepository)

        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]

        binding.loginTextviewKakao.setOnClickListener{
            // 카카오톡 설치 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(
                    this@LoginActivity,
                    callback = mCallback
                )
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    this@LoginActivity,
                    callback = mCallback
                )
            }

        }
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                    Toast.makeText(this, "사용자 취소", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }
        } else if (token != null) {
            val data = AuthRequest(token.accessToken)
            // API service 카카오 로그인 후 발급받은 appToken, isNewMember 값
            loginViewModel.create(data)
            loginViewModel.jwt_data.observe(this@LoginActivity, Observer {
                when (it) {
                    is NetworkResult.Loading -> {
                    }
                    is NetworkResult.Error -> {
                        Log.d("whatisthis", "LoginActivity "+it.toString())
                    }
                    is NetworkResult.Success -> {
                        loginViewModel.saveToken(it.data!!.result!!.appToken!!)
                        Log.e("whatisthis", "로그인 성공! 토큰값 : ${token.accessToken}")
                        UserApiClient.instance.me { user, error ->
                            var email = user?.kakaoAccount?.email
                            var name = user?.kakaoAccount?.profile?.nickname
                            Log.e(
                                "whattisthis",
                                "닉네임 ${user?.kakaoAccount?.profile?.nickname}"
                            )
                            Log.e("whattisthis", "이메일 ${user?.kakaoAccount?.email}")
                            // 카카오 로그인 후 받은 카카오 데이터(email, name)를 서버에 보내서 토큰 받아오기
                            Toast.makeText(
                                this@LoginActivity,
                                "${user?.kakaoAccount?.profile?.nickname}님 환영합니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            })

        }
    }
}