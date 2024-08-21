package com.example.quotes.activities.register

import android.net.Uri
import android.util.Log
import com.example.quotes.Model.User
import com.example.quotes.utils.Utils.isValidFacebookLink
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterPresenter(val view: IRegisterView) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    fun updateUserData(user: User) {
        val linkFB = user.linkFB
        when {
            user.name.isNullOrEmpty() -> view.showError("Nhập tên của bạn!")
            (!linkFB.isNullOrEmpty() && !isValidFacebookLink(linkFB)) -> view.showError("Link Facebook không hợp lệ!")
            else -> {
                view.showLoading()
                if (!(!user.image.isNullOrEmpty() && user.image!!.startsWith("http"))) {
                    uploadProfileImage(user.id!!, Uri.parse(user.image)) { imageUrl ->
                        if (imageUrl != null) {
                            user.image = imageUrl.toString()
                            saveUserData(user,true)
                        } else {
                            view.showError("Không thể tải ảnh lên")
                            view.hideLoading()
                        }
                    }
                } else {
                    saveUserData(user,true)
                }
            }
        }
    }


    fun register(user: User) {
        when {
            user.email.isNullOrEmpty() -> view.showError("Nhập email của bạn!")
            user.name.isNullOrEmpty() -> view.showError("Nhập tên của bạn!")
            user.password.isNullOrEmpty() -> view.showError("Nhập mật khẩu của bạn!")
            else -> {
                view.showLoading()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        firebaseAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                            .await()
                        user.id = firebaseAuth.currentUser?.uid
                        user.id?.let { userId ->
                            uploadProfileImage(userId, Uri.parse(user.image)) { imageUrl ->
                                user.image = imageUrl
                                saveUserData(user, false)
                            }
                        } ?: withContext(Dispatchers.Main) {
                            view.showError("Failed to retrieve user ID")
                            view.hideLoading()
                        }
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        withContext(Dispatchers.Main) {
                            view.showError("Email hoặc mật khẩu không hợp lệ!")
                            view.hideLoading()
                        }
                    } catch (e: FirebaseNetworkException) {
                        withContext(Dispatchers.Main) {
                            view.showError("Không có kết nối mạng!")
                            view.hideLoading()
                        }
                    } catch (e: FirebaseAuthUserCollisionException) {
                        withContext(Dispatchers.Main) {
                            view.showError("Email đã được sử dụng!")
                            view.hideLoading()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            view.showError("Lỗi không xác định!")
                            view.hideLoading()
                        }
                    }
                }
            }
        }
    }


    private fun uploadProfileImage(
        userId: String,
        imageUri: Uri?,
        onImageUploaded: (String?) -> Unit
    ) {
        // Kiểm tra imageUri null
        imageUri?.let {
            val storageReference = storage.getReference("Profile/$userId/Profile.jpg")
            storageReference.putFile(it)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        onImageUploaded(uri.toString())
                    }
                }
                .addOnFailureListener {
                    onImageUploaded(null)
                }
        } ?: onImageUploaded(null)  // Xử lý trường hợp imageUri null
    }


    private fun saveUserData(user: User, updateMode: Boolean) {
        val userData = hashMapOf(
            "name" to user.name,
            "image" to user.image,
            "linkFB" to user.linkFB
        )

        firestore.collection("Users").document(user.id!!).set(userData)
            .addOnSuccessListener {
                if (updateMode) {
                    view.showError("Cập nhật thành công!")
                    view.backUpdateUser()
                } else {
                    view.showError("Registration successful")
                    view.navigateToLogin()
                }
            }
            .addOnFailureListener {
                view.showError("Có lỗi xảy ra!")
                view.hideLoading()

            }
    }


}