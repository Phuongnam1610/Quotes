package com.example.quotes.fragments.bottomcomment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.quotes.Model.Comment
import com.example.quotes.Model.Post
import com.example.quotes.adapters.CommentAdapter
import com.example.quotes.databinding.BottomCommentBinding
import com.example.quotes.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class BottomCommentFragment : SuperBottomSheetFragment(), IBottomCommentView {
    private lateinit var binding: BottomCommentBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var presenter: BottomCommentPresenter
    private lateinit var post: Post
    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

    override fun getExpandedHeight(): Int {
        return 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomCommentBinding.inflate(inflater)
        setUp()
        binding.btncomment.setOnClickListener {
            val comment = Comment(null, post.id, binding.edtcomment.text.toString(), FirebaseAuth.getInstance().currentUser!!.uid, post.userid!!)
            if(commentAdapter.editting!=""){
                comment.id=commentAdapter.editting
                presenter.updateComment(comment)
            }
            else{
            presenter.upComment(comment)}
            binding.edtcomment.text.clear()
        }
        return binding.root
    }

    override fun displayListComment(comments: List<Comment>, mode: Int) {
        if (mode == 0) {
            commentAdapter.listcomments.addAll(comments)
            commentAdapter.notifyItemRangeInserted(0, comments.size)
        }
        commentAdapter.listcomments.sortByDescending { it.createdat }

    }

    override fun refreshComment(lcm: MutableList<Comment>, mode: Int) {
        when (mode) {
            0 -> {
                commentAdapter.listcomments.addAll(0, lcm)
                commentAdapter.notifyItemRangeInserted(0, lcm.size)
                if (lcm.any { it.userid == FirebaseAuth.getInstance().currentUser?.uid }) {
                    binding.rvcomment.smoothScrollToPosition(0)
                }
            }
            1 -> {
                val comment = commentAdapter.listcomments.find { it.id == lcm[0].id }
                val index = commentAdapter.listcomments.indexOf(comment)
                commentAdapter.listcomments[index]= lcm[0]
                commentAdapter.editting=""
                commentAdapter.notifyItemChanged(index)
            }
            2 -> {
                val comment = commentAdapter.listcomments.find { it.id == lcm[0].id }
                val index=commentAdapter.listcomments.indexOf(comment)
                commentAdapter.listcomments.removeAt(index)
                commentAdapter.notifyItemRemoved(index)
            }
        }
    }

    override fun onEditComment(comment: Comment) {
        binding.edtcomment.setText(comment.content)
    }

    override fun setUp() {
        commentAdapter = CommentAdapter(this, mutableListOf())
        post = arguments?.getParcelable<Post>("post")!!
        presenter = BottomCommentPresenter(this)
        binding.rvcomment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvcomment.adapter = commentAdapter
        presenter.addListenerComment(post.id!!)
    }


    override fun showError(message: String?) {
        context?.let { Utils.showToast(it, message!!) }
    }

    override fun onFinish() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }


}

