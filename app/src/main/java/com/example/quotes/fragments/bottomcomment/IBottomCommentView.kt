package com.example.quotes.fragments.bottomcomment
import com.example.quotes.Model.Comment
import com.example.quotes.base.IMVPView

interface IBottomCommentView : IMVPView {

    fun displayListComment(comments: List<Comment>,mode:Int)
    fun refreshComment(lcm:MutableList<Comment>,mode:Int)
    fun onEditComment(Comment:Comment)

}