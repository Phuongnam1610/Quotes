package com.example.quotes.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.Model.Notification
import com.example.quotes.databinding.ItemCommentNotifBinding

import com.example.quotes.fragments.notif.INotifView
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.UserRepository
import com.example.quotes.utils.Utils
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class NotifAdapter(val view: INotifView, val listnotifications: MutableList<Notification>) :
    RecyclerView.Adapter<NotifAdapter.NotifViewHolder>() {

    class NotifViewHolder(
        val view: INotifView,
        val context: Context,
        val binding: ItemCommentNotifBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val userRepository = UserRepository()
        private val postRepository = PostRepository()

        fun setnotifData(notification: Notification) {
            GlobalScope.launch(Dispatchers.IO) {
                val user = userRepository.getUserById(notification.userid2!!)
                CoroutineScope(Dispatchers.Main).launch {
                    if (user != null) {
                        binding.tvusername.text = user.name

                        Utils.loadImageFromUri(
                            context,
                            binding.imgavatar,
                            Uri.parse(user.getImageUrl()),
                            null
                        )

                    }
                }


            }

// Chuyển đổi timestamp sang đối tượng Date
            val timestamp = notification.time as Timestamp
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()

            binding.tvtime.text = date
            var message = ""
            if (notification.type == 0) {
                message = "Đã thích bài viết của bạn"
            } else if (notification.type == 1) {
                message = "Đã nhận xét về bài viết của bạn"
            } else {
                message = "Đã báo cáo bài viết của bạn"
            }
            binding.tvcommentnotif.text = message
            binding.root.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val post = postRepository.getPostByPostID(notification.postid!!)
                    withContext(Dispatchers.Main) {
                        if (post != null) {
                            post.id = notification.postid

                            view.showQuotesFragment(post)

                        } else {
                            view.showError("Bài viết đã bị xóa!")
                        }
                    }
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {

        return NotifViewHolder(
            view,
            parent.context,
            ItemCommentNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount(): Int {
        return listnotifications.size
    }

    internal fun NotifyDataSetChanged() {
        notifyDataSetChanged()
    }

    internal fun clearNotificationList() {
        listnotifications.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        holder.setnotifData(listnotifications[position])

    }

}