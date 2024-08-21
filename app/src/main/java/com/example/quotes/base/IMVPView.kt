package com.example.quotes.base

import android.content.Context
import com.example.quotes.Model.Category

interface IMVPView {
    fun setUp()
    fun showLoading()
    fun hideLoading()
    fun showError(message: String?)
    open fun onFinish()

}