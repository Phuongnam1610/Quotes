package com.example.quotes.fragments.bottomcmtset

import com.example.quotes.Model.Comment

interface iCallbackCommentSet {
    fun onDeleteComment(comment: Comment)
    fun onEditComment(comment: Comment)
}