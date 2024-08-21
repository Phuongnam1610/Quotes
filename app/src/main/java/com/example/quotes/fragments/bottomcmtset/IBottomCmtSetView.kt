package com.example.quotes.fragments.bottomcomment
import com.example.quotes.Model.Comment
import com.example.quotes.base.IMVPView

interface IBottomCmtSetView : IMVPView {
    fun onDeleteComment()
    fun onEditComment()
}