package com.example.quotes.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.quotes.activities.forgotpass.ForgotPassActivity
import com.example.quotes.activities.register.RegisterActivity
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityLoginBinding
import com.example.quotes.activities.home.HomeActivity

class LoginActivity : BaseActivity() , ILoginView {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter

    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE
        binding.loutmain.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
        binding.loutmain.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)
        binding.apply {
            btnlogin.setOnClickListener {
                val email=binding.edtemail.text.toString()
                val password=binding.edtpassword.text.toString()
                presenter.login(email,password)
            }
            btnregister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnforgot.setOnClickListener{
                val intent = Intent(this@LoginActivity, ForgotPassActivity::class.java)
                startActivity(intent)
            }

        }

    }
    override fun navigateToHome() {
        hideLoading()
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

    override fun setUp() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        presenter = LoginPresenter(this)
    }

}