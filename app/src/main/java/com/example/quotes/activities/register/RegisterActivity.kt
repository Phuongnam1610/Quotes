package com.example.quotes.activities.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quotes.Model.User
import com.example.quotes.activities.forgotpass.ResetPassActivity
import com.example.quotes.activities.login.LoginActivity
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityRegisterBinding
import com.example.quotes.utils.Utils


class RegisterActivity : BaseActivity(), IRegisterView {
    private lateinit var binding: ActivityRegisterBinding
    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE
        binding.loutmain.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
        binding.loutmain.visibility = View.VISIBLE
    }

    private var userImage: Uri? = null
    private val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                userImage = uri
                if (updateMode) {
                    binding.imavatarU.setImageURI(userImage)
                } else {
                    binding.imavatar.setImageURI(userImage)
                }
            }
        }
    private lateinit var presenter: RegisterPresenter
    private var user: User? = null
    private var updateMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)
        binding.apply {
            imavatar.setOnClickListener {
                selectImage.launch("image/*")
            }
            imavatarU.setOnClickListener {
                selectImage.launch("image/*")
            }
            btnregister.setOnClickListener {
                val email = binding.edtemail.text.toString().trim()
                val password = binding.edtpassword.text.toString().trim()
                val name = binding.edtusername.text.toString().trim()
                val linkFB = binding.edtlinkFB.text.toString().trim()
                user = User(user?.id, email, password, name, userImage.toString(), linkFB)
                presenter.register(user!!)
            }
            btnupdate.setOnClickListener {
                val name = binding.edtusernameU.text.toString().trim()
                val linkFB = binding.edtlinkFBU.text.toString().trim()
                user = User(user?.id, null, null, name, userImage.toString(), linkFB)
                presenter.updateUserData(user!!)
            }
            tvresetpass.setOnClickListener{
                val intent =Intent(this@RegisterActivity,ResetPassActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun navigateToLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    override fun backUpdateUser() {
        finish()
    }

    override fun setUp() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        presenter = RegisterPresenter(this)
        user = intent.getParcelableExtra("user")
        if (user != null) {
            binding.apply {
                loutupdate.visibility = View.VISIBLE
                loutregister.visibility = View.GONE
                edtusernameU.setText(user?.name)
                edtlinkFBU.setText(user?.linkFB)
                btnregister.text = "Cập nhật"
            }
            if (user!!.image == null) {
                userImage = null
            } else {
                userImage = Uri.parse(user?.image)
                Utils.loadImageFromUri(this, binding.imavatarU, Uri.parse(user?.getImageUrl()), null)
            }
            updateMode = true
        }

    }


}