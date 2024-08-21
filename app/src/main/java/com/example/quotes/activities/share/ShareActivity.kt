package com.example.quotes.activities.share

import android.graphics.Bitmap
import android.os.Bundle
import com.example.quotes.Model.Post
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityShareBinding


class ShareActivity : BaseActivity(), IShareView {
    private lateinit var binding: ActivityShareBinding
    private lateinit var presenter: SharePresenter
    private lateinit var post: Post
    private var bm: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        presenter.createBitmap(post);
        binding.apply {
            btnfb.setOnClickListener {
                presenter.shareBitmap(bm)
            }
            btnsave.setOnClickListener {
                presenter.saveBitmap(bm)
            }
        }



        setContentView(binding.root)
    }

    override fun displayBitmapShare(bitmap: Bitmap) {
        bm = bitmap
        binding.imgbitmap.setImageBitmap(bitmap)
    }


    override fun finishA() {
        finish()
    }

    override fun setUp() {
        binding = ActivityShareBinding.inflate(layoutInflater)
        presenter = SharePresenter(this)
        post = intent.getParcelableExtra("post")!!
    }


}