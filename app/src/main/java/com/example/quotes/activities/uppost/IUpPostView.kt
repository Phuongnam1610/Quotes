package com.example.quotes.activities.uppost

import android.net.Uri
import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.base.IMVPView

interface IUpPostView : IMVPView {
    fun setCategories(listCategories: List<Category>)
    fun setImage(imageUri: Uri?)
    fun clear()
    fun hideUpPost()
    fun showUpPost()
    fun backUpdatePost(post: Post)


}