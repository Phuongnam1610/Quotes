package com.example.quotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.databinding.ItemContainerQuotesSearchBinding
import com.example.quotes.fragments.search.ISearchView

class PostItemAdapter(val listCategory:MutableList<Category>, val view:ISearchView, val listpost:MutableList<Post>):RecyclerView.Adapter<PostItemAdapter.QuotesViewHolder>() {


    class QuotesViewHolder(val listCategory:MutableList<Category>, val context: Context, val binding: ItemContainerQuotesSearchBinding): RecyclerView.ViewHolder(binding.root){


        fun setQuotesData(post: Post){
            binding.tvauthor.text = post.author
            binding.tvquotes.text = post.content
            binding.tvtag.text=(listCategory.find { it.id==post.categoryid })?.name.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
    return QuotesViewHolder(listCategory,parent.context, ItemContainerQuotesSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
        holder.binding.root.setOnClickListener {
            view.showQuotesActivity(listpost[position])
        }
    }
}