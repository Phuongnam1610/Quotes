package com.example.quotes.activities.forgotpass

import com.example.quotes.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPassPresenter (val view: IForgotPassView) {
    fun resetPassword(email:String) {
        if(email.isNullOrEmpty()){
            view.showError("không được để trống email")
            return;
        }
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val c= Utils.forgotPassword(email)
            withContext(Dispatchers.Main){
                if(c){
                    view.showError("Yêu cầu quên mật khẩu đã được gửi về mail")
                    view.onFinish()
                }
                else{
                    view.showError("Không thể gửi yêu cầu quên mật khẩu")
                    view.hideLoading()
                }
            }
        }

    }
}