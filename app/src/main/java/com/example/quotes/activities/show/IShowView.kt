package com.example.quotes.activities.show

import com.example.quotes.Model.Post
import com.example.quotes.base.IMVPView

interface IShowView:IMVPView {
    fun displayPost(post: Post)
}