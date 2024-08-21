package com.example.quotes.activities.uppost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.R
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityUpPostBinding
import com.example.quotes.utils.Utils


class UpPostActivity : BaseActivity(), IUpPostView {
    private lateinit var listCategories: List<Category>
    private lateinit var binding: ActivityUpPostBinding
    private lateinit var presenter: UpPostPresenter
    private var post:Post? = null

    private var mode = false
    private var imguri: Uri? = null
    private val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri!=null){
            imguri = uri
            binding.imgquotes.setImageURI(uri)}
        }


    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE
        binding.layoutmain.visibility = View.GONE
    }


    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
        binding.layoutmain.visibility = View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContentView(binding.root)
        presenter.loadCategories()
        presenter.checkUpPost()
        binding.apply {
            btnpost.visibility=View.GONE
            imgquotes.setOnClickListener {
                selectImage.launch("image/*")
            }
            btnpost.setOnClickListener {
                val content = edtquotes.text.toString()
                val author = edtauthor.text.toString()
                val selectedIndex = binding.spinner.selectedItemPosition
                val selectedCategory = listCategories[selectedIndex]
                val categoryId = selectedCategory.id
                val post2 = Post(
                    post?.id,
                    content,
                    author,
                    null,
                    categoryId,
                    null,
                    null,
                    imguri.toString(),
                    null,
                    null
                )

                presenter.upPost(
                    mode, post2,
                )
            }

        }
    }

    override fun setCategories(listCategories: List<Category>) {
        val adapter = ArrayAdapter(
            (this),
            R.layout.item_spinner,
            listCategories.map { it.name }
        )
        adapter.setDropDownViewResource(R.layout.item_spinner)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(binding.spinner.count - 1)
        this.listCategories = listCategories
        setupPostUpdate()
    }
    
    


    override fun setImage(imageUri: Uri?) {

    }

    override fun clear() {
        binding.edtauthor.text = null
        binding.edtquotes.text = null

    }

    override fun hideUpPost() {
        binding.btnpost.visibility=View.GONE
        showError("bạn bị chặn đăng bài")
    }

    override fun showUpPost() {
        binding.btnpost.visibility=View.VISIBLE
    }

    override fun backUpdatePost(post: Post) {
        showError("Cập nhật thành công")
        val intent = Intent()
        intent.putExtra("post", post)
        setResult(RESULT_OK, intent)
        finish()
    }
    
    fun setupPostUpdate(){
        if (post != null) {
            val categoryIndex = listCategories.indexOfFirst { it.id == post!!.categoryid }
            binding.apply {
                spinner.setSelection(categoryIndex)
                edtauthor.setText(post!!.author)
                edtquotes.setText(post!!.content)
                val pencilDrawable = ContextCompat.getDrawable(this@UpPostActivity, R.drawable.pencil)
                btnpost.icon = (pencilDrawable)
                tvchiase.text = "Cập nhật"
            }

            if(post!!.image!=null){
                imguri = Uri.parse(post!!.image)
                Utils.loadImageFromUri(this, binding.imgquotes, imguri, null)
            }
            else{
                imguri=null
            }
            mode = true
           
        }
    }

    override fun setUp() {
        binding = ActivityUpPostBinding.inflate(layoutInflater)
        presenter = UpPostPresenter(this)
        post = intent.getParcelableExtra("post")
        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        if (text != null) {
            binding.edtquotes.setText(text.toString())
        }

    }

}