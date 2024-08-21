package com.example.quotes.utils

import FCMService
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.quotes.AccessToken
import com.example.quotes.Model.Post
import com.example.quotes.R
import com.example.quotes.databinding.CustomlayoutBinding
import com.example.quotes.repository.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Response


object Utils {
    fun createBitmap(context: Context, p: Post, callback: (bm: Bitmap?) -> Unit) {
        val font = ResourcesCompat.getFont(context, R.font.htl)
        val customLayout = CustomlayoutBinding.inflate(LayoutInflater.from(context))
        customLayout.tvquotes.typeface = font
        customLayout.tvauthor.typeface = font
        customLayout.tvquotes.text = p.content
        customLayout.tvauthor.text = p.author

        loadImageFromUri(context, customLayout.imgquotes, Uri.parse(p.getImageUrl())) { bm ->
            if (bm != null) {
                // Lấy kích thước màn hình
                val displayMetrics = context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels

                // Tạo bitmap với kích thước tương ứng với màn hình
                val bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)

                // Tạo canvas để vẽ layout lên bitmap
                val canvas = Canvas(bitmap)

                // Đặt layout vào giữa canvas
                customLayout.root.measure(
                    View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.EXACTLY)
                )
                customLayout.root.layout(0, 0, screenWidth, screenHeight)
                customLayout.root.draw(canvas)
                callback.invoke(bitmap)

            } else {
                callback.invoke(null)
            }

        }

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun isValidFacebookLink(link: String): Boolean {
        return link.startsWith("http://") || link.startsWith("https://")
    }

    fun loadImageFromUri(
        context: Context,
        imageView: ImageView,
        uri: Uri?,
        onBitmapLoaded: ((Bitmap?) -> Unit)?,
    ) {
        if (context is Activity && (context.isDestroyed || context.isFinishing)) {
            return
        }
        if (context is BottomSheetDialogFragment && !context.isVisible) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                    onBitmapLoaded?.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun checkPermissions(context: Context): Boolean {
        val writePermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                123
            )
            return false
        }
        return true
    }

    fun sendNotification(userID: String, userID2: String, mode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = UserRepository().getUserById(userID)
                val user2 = UserRepository().getUserById(userID2)
                val token = user2!!.token
                val title = "${user!!.name}"
                var body = ""
                when (mode) {
                    0 -> {
                        body = "Đã thích bài viết của bạn"
                    }

                    1 -> {
                        body = "Đã bình luận về bài viết của bạn"
                    }
                }
                val fcmS = FCMService(AccessToken().getAccessToken().toString())
                fcmS.sendPushNotification(token!!, title, body)
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>,
                        ) {

                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        }

                    })
            } catch (e: Exception) {

            }
        }


    }

    suspend fun updateFieldImage(docid: String, image: String?): Boolean {
        return try {
            FirebaseFirestore.getInstance().collection("Posts").document(docid)
                .update("image", image).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun upImagePost(post: Post): Boolean {
        return try {
            val storageReference = Firebase.storage.reference.child("Posts/${post.id!!}")
            storageReference.putFile(Uri.parse(post.image)).await()
            val uri = storageReference.downloadUrl.await()
            updateFieldImage(post.id!!, uri.toString())
            true
        } catch (e: Exception) {
            false

        }
    }

    suspend fun handleImage(post: Post): Boolean {
        return try {
            if (post.image == null || post.image == "null") {
                updateFieldImage(post.id!!, "null")
            } else {
                if ((post.image)?.startsWith("http") == false) {
                    upImagePost(post)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun forgotPassword(email: String): Boolean {
        return try {
            // Gửi email đặt lại mật khẩu
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun resetPassword( oldpass: String, newPass: String): Boolean {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            val credential = EmailAuthProvider.getCredential(user?.email!!, oldpass)
            user?.reauthenticate(credential)?.await()
            user?.updatePassword(newPass)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }
}







