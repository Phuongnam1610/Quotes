package com.example.quotes.activities.login

import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginPresenter(var view: ILoginView) {
    private val auth = FirebaseAuth.getInstance()

    private suspend fun updateUserToken(userId: String) {
        val token = FirebaseMessaging.getInstance().token.await()
        val updateData = hashMapOf("token" to token)
        FirebaseFirestore.getInstance().collection("Users").document(userId).update(updateData as Map<String, Any>).await()
    }
    fun login(email: String, password: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("Email không hợp lệ")
        } else if (password.isNullOrEmpty()) {
            view.showError("Mật khẩu không được để trống")
        } else {
            view.showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                var currentUser: String?
                try {
                    val authResult = auth.signInWithEmailAndPassword(email, password).await()
                    currentUser = authResult.user?.uid
                    if (currentUser != null) {
                        updateUserToken(currentUser)
                        withContext(Dispatchers.Main) {
                            view.showError("Đăng nhập thành công!")
                            view.navigateToHome()
                        }
                    }
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    withContext(Dispatchers.Main) {
                        view.showError("Email hoặc mật khẩu không hợp lệ!")
                        view.hideLoading()
                    }
                } catch (e: FirebaseNetworkException) {
                    withContext(Dispatchers.Main) {
                        view.showError("Không có kết nối mạng!")
                        view.hideLoading()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        view.showError("Lỗi không xác định!")
                        view.hideLoading()
                    }
                }

            }

        }
    }


}