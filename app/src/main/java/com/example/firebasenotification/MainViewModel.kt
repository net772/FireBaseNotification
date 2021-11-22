package com.example.firebasenotification

import android.content.Context
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private lateinit var mContext: Context
    private lateinit var mNavigator: BaseNavigator

    fun init(applicationContext: Context, navigator: BaseNavigator) {
        mContext = applicationContext
        mNavigator = navigator
    }
}