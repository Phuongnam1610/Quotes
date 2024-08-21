package com.example.quotes.activities.showuser

import android.content.Intent
import android.net.Uri
import com.example.quotes.repository.CategoryRepository
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowUserPresenter(val view: IShowUserView) {
    private var userRepository: UserRepository = UserRepository()

    fun getCurrentUser() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val user = userRepository.getUserById(FirebaseAuth.getInstance().currentUser?.uid!!)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    user.id = FirebaseAuth.getInstance().currentUser?.uid
                    view.showUser(user)
                    view.hideLoading()
                }
                else{
                    view.showError("Có lỗi khi load người dùng!")
                    view.hideLoading()
                }
            }

        }

    }

    fun getAllPostByUserId(userId: String) {
        view.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            val posts =
                PostRepository().getListPostsByUserid(userId)
            withContext(Dispatchers.Main) {
                view.displayPostList(posts)
                view.hideLoading()
            }
        }
    }


    fun logOut() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val c = UserRepository().deleteToken()
            withContext(Dispatchers.Main) {
                if (c) {
                    FirebaseAuth.getInstance().signOut()
                    view.vSingOut()
                } else {
                    view.showError("Có lỗi khi đăng xuất!")
                    view.hideLoading()

                }
            }
        }


    }

    fun openFB(linkFB: String?) {
        if (linkFB == null || linkFB == "") {
            view.showError("Chưa có link Facebook")
        } else {
            view.openLinkFB(linkFB)
        }
    }

    fun loadCategories() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val list = CategoryRepository().loadCategories()
            withContext(Dispatchers.Main) {
                view.setCategories(list)
                view.hideLoading()
            }
        }
    }
}
