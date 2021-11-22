package com.example.firebasenotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.firebasenotification.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId() = R.layout.activity_main
    private val viewModel : MainViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.vm = viewModel
        viewModel.init(applicationContext, this)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }
}