package com.example.quotes.fragments.bottomcomment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.quotes.Model.Comment
import com.example.quotes.Model.Post
import com.example.quotes.adapters.CommentAdapter
import com.example.quotes.databinding.BottomCmtSetBinding
import com.example.quotes.fragments.bottomcmtset.iCallbackCommentSet
import com.example.quotes.utils.Utils


class BottomCmtSetFragment : SuperBottomSheetFragment() {
    private lateinit var binding: BottomCmtSetBinding
    public lateinit var iCallbackCommentSet: iCallbackCommentSet
    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

    override fun getExpandedHeight(): Int {
        return 500
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomCmtSetBinding.inflate(inflater)
        val comment  = arguments?.getParcelable<Comment>("cmt")!!
        binding.apply {
            tvedit.setOnClickListener{
                iCallbackCommentSet.onEditComment(comment)
                dismiss()
            }
            tvdelete.setOnClickListener{
                iCallbackCommentSet.onDeleteComment(comment)
                dismiss()
            }
        }
        return binding.root
    }





}

