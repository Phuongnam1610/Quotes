package com.example.quotes.fragments.home

import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.ReportRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePresenter(val view: IHomeView)  {
    private var listposts: List<Post> = emptyList()
    private var postRepository: PostRepository=PostRepository()
    fun loadPostViewPager(){
        view.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            listposts = postRepository.getAllPosts()
            withContext(Dispatchers.Main){
            view.hideLoading()
            view.displayPostList(listposts)
            }
        }
    }



}