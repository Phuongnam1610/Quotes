package com.example.quotes.fragments.search

import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.base.IMVPView

interface ISearchView :IMVPView{
    fun setCategories(listCategories: MutableList<Category>)
    fun displayPostList(listPosts:MutableList<Post>)
    fun showQuotesActivity(post: Post)

}