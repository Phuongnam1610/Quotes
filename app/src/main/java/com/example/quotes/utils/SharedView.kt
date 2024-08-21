package com.example.quotes.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.example.quotes.Model.Like
import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.R
import com.example.quotes.activities.share.ShareActivity
import com.example.quotes.activities.show.IShowView
import com.example.quotes.activities.show.ShowPresenter
import com.example.quotes.activities.showuser.ShowUserActivity
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ItemContainerQuotesBinding
import com.example.quotes.fragments.bottomcomment.BottomCmtSetFragment
import com.example.quotes.fragments.bottomcomment.BottomCommentFragment
import com.example.quotes.fragments.bottomcomment.BottomSettingFragment
import com.example.quotes.repository.LikeRepository
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class SharedView @JvmOverloads constructor(
    context: Context,post: Post,  attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IShowView , ICallbackPostUpdate{
    private var presenter: ShowPresenter
    private val binding: ItemContainerQuotesBinding
    init {

        binding = ItemContainerQuotesBinding.inflate(LayoutInflater.from(context), this, true)
        presenter = ShowPresenter(this);
        presenter.loadPost(post);

        PostRepository().addLikeCountListener(post.id!!){
            binding.tvcountheart.text=it.toString();
        }

        CoroutineScope(Dispatchers.IO).launch{
            post.liked = PostRepository().checkUserLikePost(post.id!!)
            withContext(Dispatchers.Main) {
                if (post.liked == true) {
                    binding.btnheart.setImageResource(R.drawable.liked)
                    binding.btnheart.tag = "Liked"
                } else {
                    binding.btnheart.setImageResource(R.drawable.unlike)
                    binding.btnheart.tag = "Unlike"
                }
            }
        }
        Utils.loadImageFromUri(context, binding.imgquotes, Uri.parse(post.getImageUrl()), null)
        CoroutineScope(Dispatchers.IO).launch {
            val user= UserRepository().getUserById(post.userid!!)
            withContext(Dispatchers.Main) {
                if(user!=null){
                    user.id=post.userid
                    Utils.loadImageFromUri(context, binding.imguser, Uri.parse(user.getImageUrl()), null)
                    binding.imguser.setOnClickListener {
                        val intent = Intent(context, ShowUserActivity::class.java)
                        intent.putExtra("user",user)
                        startActivity(context,intent,null)
                    }
                }
            }
        }
        binding.apply {
            btnheart.setOnClickListener{
                btnheart.isEnabled = false
                val like= Like()
                like.postid = post.id!!
                like.userid = FirebaseAuth.getInstance().currentUser!!.uid
                if (btnheart.tag == "Liked") {
                    CoroutineScope(Dispatchers.IO).launch {
                        LikeRepository().unlike(like)
                    }
                    btnheart.setImageResource(R.drawable.unlike)
                    btnheart.tag = "Unlike"
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        LikeRepository().addlike(post.userid!!, like)
                    }
                    btnheart.setImageResource(R.drawable.liked)
                    btnheart.tag = "Liked"
                }
                btnheart.isEnabled = true
            }
            btncomment.setOnClickListener{
                showDialogComment(
                    context, post
                )
            }
            btnsetting.setOnClickListener{
                showDialogSetting(
                    context, post, this@SharedView
                )
            }
            btnshare.setOnClickListener{
                val intent = Intent(context, ShareActivity::class.java)
                intent.putExtra("post",post)
                context.startActivity(intent)
            }
        }
    }


    override fun showError(message: String?) {
        (context as BaseActivity).showError(message)
    }

    override fun onFinish() {
        (context as BaseActivity).finish()
    }

    override fun displayPost(post: Post) {
        binding.tvauthor.text = post.author
        binding.tvquotes.text = post.content
    }

    override fun setUp() {
    }

    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE
        binding.layoutmain.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
        binding.layoutmain.visibility = View.VISIBLE
    }

    override fun onUpdatePost(post: Post) {
        displayPost(post)
    }

    override fun onDeletePost(postID:String) {
        presenter.deletePost(postID)
    }

    override fun onReportPost(report:Report) {
        presenter.reportPost(report)
    }

    private fun showDialogComment(context: Context, post: Post) {
        val fragment = BottomCommentFragment()
        val bundle = Bundle().apply {
            putParcelable("post", post)
        }
        fragment.arguments = bundle
        fragment.show((context as BaseActivity).supportFragmentManager, "BottomCommentFragment")
    }

    private fun showDialogSetting(context: Context, post: Post, iCallbackPostUpdate: ICallbackPostUpdate?) {
        val fragment = BottomSettingFragment()
        fragment.setCallback(iCallbackPostUpdate!!)
        val bundle = Bundle().apply {
            putParcelable("post", post)
        }
        fragment.arguments = bundle
        fragment.show((context as BaseActivity).supportFragmentManager, "BottomSettingFragment")
    }

}
interface ICallbackPostUpdate {
    fun onUpdatePost(post: Post){

    }
    fun onDeletePost(postID: String){

    }
    fun onReportPost(report: Report){}
}
