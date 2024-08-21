package com.example.quotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.databinding.ActivityShowQuotesBinding
import com.example.quotes.fragments.home.IHomeView
import com.example.quotes.utils.ICallbackPostUpdate
import com.example.quotes.utils.SharedView

class PostVPAdapter(val view: IHomeView, val listpost: MutableList<Post>) :ICallbackPostUpdate,
    RecyclerView.Adapter<PostVPAdapter.QuotesViewHolder>() {

    class QuotesViewHolder(val context: Context, val binding: ActivityShowQuotesBinding,val iCallbackPostUpdate: ICallbackPostUpdate) :
        RecyclerView.ViewHolder(binding.root) {
        fun setQuotesData(post: Post) {
            val sharedView =object :SharedView(context, post ){
                override fun onFinish() {
                }

                override fun onDeletePost(postID: String) {
                    super.onDeletePost(postID)
                    iCallbackPostUpdate.onDeletePost(postID)
                }

                override fun onReportPost(report: Report) {
                    super.onReportPost(report)
                    iCallbackPostUpdate.onReportPost(report)
                }

                override fun onUpdatePost(post: Post) {
                    super.onUpdatePost(post)
                    iCallbackPostUpdate.onUpdatePost(post)

                }
            }

            binding.main.addView(sharedView)
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {

        return QuotesViewHolder(
            parent.context,
            ActivityShowQuotesBinding.inflate(LayoutInflater.from(parent.context), parent, false),this
        )
    }

    override fun getItemCount(): Int {
        return listpost.size
    }

    internal fun addPostToList(post: List<Post>) {
        listpost.clear()
        listpost.addAll(post)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        holder.setQuotesData(listpost[position])
    }


    override fun onUpdatePost(post: Post) {
        super.onUpdatePost(post)
        val postIndex = listpost.indexOfFirst { it.id == post.id }
        if (postIndex != -1) {
            listpost[postIndex] = post
            notifyItemChanged(postIndex)
            }
    }

    override fun onDeletePost(postID: String) {
        deletePostNF(postID)
    }

    override fun onReportPost(report: Report) {
        deletePostNF(report.postid)
    }

    fun deletePostNF(postID: String) {
        val position = listpost.indexOfFirst { it.id == postID }
        if (position != -1) {
            listpost.removeAt(position)  // Xóa bài viết khỏi danh sách
            notifyItemRemoved(position)  // Thông báo cho adapter về việc xóa mục
        }
    }



}