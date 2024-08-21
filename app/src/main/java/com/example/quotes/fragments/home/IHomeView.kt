package com.example.quotes.fragments.home
import com.example.quotes.Model.Post
import com.example.quotes.base.IMVPView

interface IHomeView : IMVPView {

    fun displayPostList(posts: List<Post>)
    fun scrollToPosition(index:Int)
    fun refreshListPost()
}