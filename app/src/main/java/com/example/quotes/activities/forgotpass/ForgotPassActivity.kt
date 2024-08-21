package com.example.quotes.activities.forgotpass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quotes.R
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityForgotPassBinding

class ForgotPassActivity : BaseActivity(), IForgotPassView {
    private lateinit var binding:ActivityForgotPassBinding
    private lateinit var presenter:ForgotPassPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)
        binding.apply{
            btnreset.setOnClickListener{
                presenter.resetPassword(binding.edtemail.text.toString())
            }
        }


        }

    override fun setUp() {
        binding=ActivityForgotPassBinding.inflate(layoutInflater)
        presenter=ForgotPassPresenter(this)
    }
}