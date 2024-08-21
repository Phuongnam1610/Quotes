package com.example.quotes.activities.uppost

import android.net.Uri

import com.example.quotes.Model.Post
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.UserRepository
import com.example.quotes.repository.CategoryRepository
import com.example.quotes.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpPostPresenter(var view: IUpPostView) {

    fun loadCategories() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val listCategories = CategoryRepository().loadCategories()
            withContext(Dispatchers.Main) {
                view.setCategories(listCategories)
                view.hideLoading()
            }
        }
    }

    fun checkUpPost() {
        CoroutineScope(Dispatchers.IO).launch {
            val c = UserRepository().banUpPost()
            withContext(Dispatchers.Main) {
                if (c) {
                    view.hideUpPost()
                } else {
                    view.showUpPost()
                }
            }
        }
    }


//    fun loadImageDefault() {
//        storage.child("peakpx.jpg")
//            .downloadUrl
//            .addOnSuccessListener { uri ->
//                view.setImage(uri)
//
//            }
//            .addOnFailureListener { exception ->
//                view.showError(exception.message.toString())
//
//            }
//    }

    fun upPost(isUpdate: Boolean, post: Post) {
        view.showLoading()
        if (post.content.isNullOrEmpty() || post.author.isNullOrEmpty() || post.categoryid.isNullOrEmpty()) {
            view.showError("Không được để trống")
            view.hideLoading()
            return
        }

        if (!isUpdate) {
            CoroutineScope(Dispatchers.IO).launch {
            val c=PostRepository().addPost(post)
                withContext(Dispatchers.Main){
                if(c){
                    view.showError("Thêm bài viết thành công!")
                    view.onFinish()
                }
                else{
                    view.showError("Có lỗi khi thêm bài viết")
                    view.hideLoading()
                }}
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
            val c=PostRepository().updatePost(post)
                withContext(Dispatchers.Main){
                if(c){
                    view.backUpdatePost(post)
                }
                else{
                    view.showError("Có lỗi khi thêm bài viết")
                    view.hideLoading()
                }}
            }
        }

    }



}