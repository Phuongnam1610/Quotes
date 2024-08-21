package com.example.quotes.fragments.bottomcomment

import com.example.quotes.Model.Comment
import com.example.quotes.repository.CommentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomCommentPresenter(val view: IBottomCommentView) {
    private var commentRepository: CommentRepository=CommentRepository()
    fun addListenerComment(postid:String){
        commentRepository.addListenerNewListComment(postid){
            lcm,mode->view.refreshComment(lcm,mode)
        }
    }

    fun upComment(comment: Comment)
    {

        if(comment.content.toString().isEmpty()){
            view.showError("Comment đang trống")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.addComment(comment)
        }

    }
    fun updateComment(comment: Comment)
    {
        if(comment.content.toString().isEmpty()){
            view.showError("Comment đang trống")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.updateComment(comment)
        }

    }


}