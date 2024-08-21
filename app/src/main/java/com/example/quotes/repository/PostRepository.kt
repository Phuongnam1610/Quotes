package com.example.quotes.repository

import com.example.quotes.Model.Post
import com.example.quotes.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val postCollection = firestore.collection("Posts")
    private val likeCountListeners = mutableMapOf<String, ((Int) -> Unit)>()
    fun addLikeCountListener(postId: String, listener: (Int) -> Unit) {
        val likeCollection = postCollection.document(postId).collection("Likes")
        likeCountListeners[postId] = listener
        likeCollection.addSnapshotListener { _, _ ->
            GlobalScope.launch(Dispatchers.IO) {
                val count = getCountLikebyPostid(postId)

                withContext(Dispatchers.Main) {

                    listener(count)
                }
            }

        }
    }


    suspend fun getAllPosts(): List<Post> {
        return try {
            val snapshot = postCollection.orderBy(
                "createdat",
                Query.Direction.DESCENDING
            ) // Sort by timestamp in descending order
                .get().await()
            val posts = mutableListOf<Post>()
            for (document in snapshot.documents) {
                val post = document.toObject(Post::class.java)
                post!!.id = document.id
                post.let {
                    if (!checkUserReportPost(document.id)) {
                        posts.add(it)
                    }

                }

            }
            posts
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deletePostByPostID(postID: String): Boolean {
        return try {
            val post = postCollection.document(postID).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

//    fun deleteNotificationByPostID(postID: String?) {
//        // Lấy tham chiếu đến collection users
//        val usersRef = FirebaseFirestore.getInstance().collection("Users")
//
//// Duyệt tất cả người dùng
//        usersRef.get()
//            .addOnSuccessListener { snapshot ->
//                for (userDoc in snapshot.documents) {
//                    // Lấy tham chiếu đến collection notification của mỗi người dùng
//                    val notificationRef = userDoc.reference.collection("Notifications")
//
//                    // Duyệt tất cả thông báo của người dùng
//                    notificationRef.get()
//                        .addOnSuccessListener { notificationSnapshot ->
//                            for (notificationDoc in notificationSnapshot.documents) {
//                                // Kiểm tra xem thông báo có postid hay không
//                                val postid = notificationDoc.getString("postid")
//
//                                if (postid == postID) {
//                                    // Xóa thông báo
//                                    notificationDoc.reference.delete()
//
//                                }
//                            }
//                        }
//
//                }
//            }
//
//
//    }

    suspend fun searchQuotesByContentCategory(
        quotes: String,
        idcategory: String,
    ): MutableList<Post> {
        return try {
            val snapshot = postCollection.get().await()
            val posts = mutableListOf<Post>()
            for (document in snapshot.documents) {
                val post = document.toObject(Post::class.java)
                post!!.id = document.id
                if (document.getString("content").toString().contains(quotes)) {
                    post.let {
                        if (!checkUserReportPost(document.id)) {
                            if (idcategory == "All") {
                                posts.add(it)
                            } else {
                                if (document.getString("categoryid").toString()
                                        .contains(idcategory)
                                ) {
                                    posts.add(it)
                                }
                            }
                        }

                    }


                }
            }
            posts
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    suspend fun getPostByPostID(postID: String): Post? {
        return try {
            val document = postCollection.document(postID)
                .get()
                .await()
            val post = document.toObject(Post::class.java)
            post
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getListPostsByUserid(userid: String): MutableList<Post> {
        return try {
            val snapshot = postCollection.orderBy("createdat", Query.Direction.DESCENDING)
                .get()
                .await()
            val posts = mutableListOf<Post>()
            for (document in snapshot.documents) {
                if (document.getString("userid").toString().contains(userid)) {
                    val post = document.toObject(Post::class.java)
                    post!!.id = document.id
                    post.let { posts.add(it) }
                }
            }
            posts
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    suspend fun addPost(post: Post): Boolean {
        return try {
            post.createdat = Timestamp.now()
            post.userid = FirebaseAuth.getInstance().currentUser!!.uid
            val postData = hashMapOf(
                "content" to post.content,
                "author" to post.author,
                "categoryid" to post.categoryid,
                "image" to post.image,
                "createdat" to post.createdat,
                "userid" to post.userid
            )
            val newPost = postCollection.add(postData).await()
            post.id = newPost.id
            Utils.handleImage(post)
            true
        } catch (e: Exception) {
            false
        }

    }

    suspend fun updatePost(post: Post): Boolean {
        return try {
            post.createdat = Timestamp.now()
            post.userid = FirebaseAuth.getInstance().currentUser!!.uid
            val postData = hashMapOf(
                "content" to post.content,
                "author" to post.author,
                "categoryid" to post.categoryid,
                "image" to post.image,
                "createdat" to post.createdat,
                "userid" to post.userid
            )
            postCollection.document(
                post.id
                !!
            ).set(postData).await()
            Utils.handleImage(post)
            true
        } catch (e: Exception) {
            false
        }

    }


    private suspend fun getCountLikebyPostid(id: String): Int {
        return try {
            val snapshot = postCollection.document(id).collection("Likes").get().await()
            return snapshot.size()
        } catch (e: Exception) {
            0
        }
    }

    suspend fun checkUserLikePost(postId: String): Boolean {
        return try {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val snapshot = postCollection.document(postId).collection("Likes")
                .whereEqualTo("userid", userId)
                .get()
                .await()
            snapshot.documents.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun checkUserReportPost(postId: String): Boolean {
        return try {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val snapshot = postCollection.document(postId).collection("Reports")
                .whereEqualTo("userid", userId)
                .get()
                .await()
            snapshot.documents.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }


}