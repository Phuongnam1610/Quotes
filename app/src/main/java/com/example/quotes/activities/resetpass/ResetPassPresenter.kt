package com.example.quotes.activities.forgotpass

import com.example.quotes.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPassPresenter (val view: IResetPassView) {
    fun resetPassword(oldPass:String,newPass:String) {
        if((oldPass).isNullOrEmpty()){
            view.showError("Vui lòng nhập mật khẩu cũ")
            return;
        }
        if((newPass).isNullOrEmpty()){
            view.showError("Vui lòng nhập mật khẩu cũ")
            return;
        }
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val c= Utils.resetPassword(oldPass,newPass)
            withContext(Dispatchers.Main){
                if(c){
                    view.showError("Đổi mật khẩu thành công")
                    view.onFinish()
                }
                else{
                    view.showError("Không thể đổi mật khẩu")
                    view.hideLoading()
                }
            }
        }

    }
}