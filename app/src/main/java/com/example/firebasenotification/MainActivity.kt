package com.example.firebasenotification

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import com.example.firebasenotification.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId() = R.layout.activity_main
    private val viewModel : MainViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.vm = viewModel
        //viewModel.init(applicationContext, this)
    }

    override fun initView() {
        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent) // 새로 들어온 인텐트로 교체
        updateResult(true)
    }

    // firebase 토큰 가져오기
    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mBinding.firebaseTokenTextView.text = task.result
                } else { }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        mBinding.resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if (isNewIntent) {
                    "(으)로 갱신했습니다."
                } else {
                    "(으)로 실행했습니다."
                }
    }
}