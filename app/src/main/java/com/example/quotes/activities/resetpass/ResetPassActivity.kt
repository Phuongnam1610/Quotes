package com.example.quotes.activities.forgotpass

import android.os.Bundle
import com.example.quotes.Model.User
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityResetPassBinding

class ResetPassActivity : BaseActivity(), IResetPassView {
    private lateinit var binding: ActivityResetPassBinding
    private lateinit var presenter:ResetPassPresenter
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)
        binding.apply{
            btnAccpet.setOnClickListener{
                val oldpass=edtnewpass.text.toString()
                val newpass=edtnewpass.text.toString()
                presenter.resetPassword(oldpass,newpass)
            }
        }


        }

    override fun setUp() {
        binding=ActivityResetPassBinding.inflate(layoutInflater)
        presenter=ResetPassPresenter(this)
    }
}