package com.example.quotes.activities.show

import android.os.Bundle
import com.example.quotes.Model.Post
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityShowQuotesBinding
import com.example.quotes.utils.SharedView


class ShowActivity : BaseActivity() {
    private lateinit var binding: ActivityShowQuotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUp()
        setContentView(binding.root)
    }
    override fun setUp() {
        binding=ActivityShowQuotesBinding.inflate(layoutInflater)
        val sharedView=SharedView(this,intent?.getParcelableExtra<Post>("post")!!)
        binding.main.addView(sharedView)
    }


}
