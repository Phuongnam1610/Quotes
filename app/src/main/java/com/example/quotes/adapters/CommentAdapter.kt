package com.example.quotes.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.Model.Comment
import com.example.quotes.Model.User
import com.example.quotes.R
import com.example.quotes.activities.home.HomeActivity
import com.example.quotes.activities.showuser.ShowUserActivity
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ItemCommentNotifBinding
import com.example.quotes.fragments.bottomcmtset.iCallbackCommentSet
import com.example.quotes.fragments.bottomcomment.BottomCmtSetFragment
import com.example.quotes.fragments.bottomcomment.BottomCommentFragment
import com.example.quotes.fragments.bottomcomment.IBottomCommentView
import com.example.quotes.repository.CommentRepository
import com.example.quotes.repository.UserRepository
import com.example.quotes.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class CommentAdapter(val view: IBottomCommentView, val listcomments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(), iCallbackCommentSet {
    public var editting: String = ""

    class CommentViewHolder(
        val context: Context,
        val binding: ItemCommentNotifBinding,
        val iCallbackCommentSet: iCallbackCommentSet,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val userRepository = UserRepository()

        fun setCommentData(comment: Comment) {
            binding.tvcommentnotif.text = comment.content
            val timestamp = comment.createdat as Timestamp
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()
            binding.tvtime.text = date
            CoroutineScope(Dispatchers.IO).launch {
                val user = userRepository.getUserById(comment.userid!!)
                 user?.id =comment.userid
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        binding.tvusername.text = user.name
                        Utils.loadImageFromUri(
                            context,
                            binding.imgavatar,
                            Uri.parse(user.getImageUrl()),
                            null
                        )
                        binding.tvusername.setOnClickListener{
                            showUserActivity(user)
                        }
                        binding.imgavatar.setOnClickListener{
                            showUserActivity(user)
                        }
                    }
                }
            }


            binding.root.setOnLongClickListener {
                if (comment.userid == FirebaseAuth.getInstance().currentUser?.uid) {
                    showCmtSet(context, comment, iCallbackCommentSet)
                }
                true
            }


        }
        fun showUserActivity(user: User){
            val intent = Intent(context, ShowUserActivity::class.java)
            intent.putExtra("user",user)
            startActivity(context,intent,null)
        }
        private fun showCmtSet(
            context: Context,
            comment: Comment,
            iCallbackCommentSet: iCallbackCommentSet,
        ) {
            val fragment = BottomCmtSetFragment()
            val bundle = Bundle().apply {
                putParcelable("cmt", comment)
            }
            fragment.arguments = bundle
            fragment.iCallbackCommentSet = iCallbackCommentSet

            fragment.show((context as BaseActivity).supportFragmentManager, "BottomCmtSet")
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            (view as BottomCommentFragment).context!!,
            ItemCommentNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            this
        )
    }

    override fun getItemCount(): Int {
        return listcomments.size
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.setCommentData(listcomments[position])
        if (listcomments[position].id == editting) {
            holder.binding.root.setCardBackgroundColor(
                ContextCompat.getColor(
                    (view as BottomCommentFragment).context!!,
                    R.color.tim
                )
            )
        } else {
            holder.binding.root.setCardBackgroundColor(
                ContextCompat.getColor(
                    (view as BottomCommentFragment).context!!,
                    R.color.main
                )
            )
        }

    }

    override fun onDeleteComment(comment: Comment) {
        CoroutineScope(Dispatchers.IO).launch {
            CommentRepository().deleteComment(comment)
        }

    }

    override fun onEditComment(comment: Comment) {
        editting = comment.id!!
        notifyItemChanged(listcomments.indexOf(comment))
        view.onEditComment(comment)

    }
}