package com.example.quotes.activities.showuser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.Model.User
import com.example.quotes.activities.login.LoginActivity
import com.example.quotes.activities.register.RegisterActivity
import com.example.quotes.activities.show.ShowActivity
import com.example.quotes.adapters.PostItemAdapter
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.FragmentUserBinding
import com.example.quotes.utils.SharedView
import com.example.quotes.utils.Utils

class ShowUserActivity : BaseActivity(), IShowUserView {
    private lateinit var binding: FragmentUserBinding
    private lateinit var presenter: ShowUserPresenter
    private lateinit var postAdapter: PostItemAdapter
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)

        binding.apply {
            btnlogout.setOnClickListener {
                presenter.logOut()
            }
            btnsetting.setOnClickListener {
                val intent = Intent(this@ShowUserActivity, RegisterActivity::class.java)
                intent.putExtra("user", user)
                startActivity( intent)
            }

            btnfb.setOnClickListener {
                presenter.openFB(user?.linkFB)

            }
        }

    }


    fun initUser() {
        user = intent.getParcelableExtra("user")
        if (user != null) {
            binding.btnlogout.visibility = View.GONE
            binding.btnsetting.visibility = View.GONE
            showUser(user!!)
        } else {
            presenter.getCurrentUser()
            binding.btnlogout.visibility = View.VISIBLE
            binding.btnsetting.visibility = View.VISIBLE
        }
    }

    override fun showUser(user: User) {
        binding.tvuser.text = user.name
        Utils.loadImageFromUri(
            this, binding.imguser, Uri.parse(user.getImageUrl()), null
        )
        this.user = user
        presenter.getAllPostByUserId(user?.id!!)


    }

    override fun displayPostList(listPosts: MutableList<Post>) {
        postAdapter.addPostToList(listPosts)
    }

    override fun vSingOut() {
        startActivity(Intent(this@ShowUserActivity, LoginActivity::class.java))
        finishAffinity()
    }

    override fun openLinkFB(linkFB:String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkFB))
        startActivity(intent)
    }

    override fun setCategories(listCategories: MutableList<Category>) {
        postAdapter = PostItemAdapter(listCategories,this, mutableListOf())
        binding.rvpost.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvpost.adapter = postAdapter
    }

    override fun showQuotesActivity(post: Post) {
        val intent = Intent(this, ShowActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }



    override fun setUp() {
        binding = FragmentUserBinding.inflate(layoutInflater)
        presenter = ShowUserPresenter(this)
        presenter.loadCategories()



    }

    override fun onResume() {
        super.onResume()
        initUser()
    }

}