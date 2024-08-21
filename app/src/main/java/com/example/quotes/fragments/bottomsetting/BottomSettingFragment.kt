package com.example.quotes.fragments.bottomcomment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.activities.uppost.UpPostActivity
import com.example.quotes.databinding.BottomSettingBinding
import com.example.quotes.utils.ICallbackPostUpdate
import com.example.quotes.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope

class BottomSettingFragment: SuperBottomSheetFragment(),IBottomSettingView{
    private lateinit var binding:BottomSettingBinding
    private lateinit var presenter: BottomSettingPresenter
    private lateinit var post:Post
    private lateinit var icallbackPostUpdate: ICallbackPostUpdate
    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            post= result.data?.getParcelableExtra("post")!!
            icallbackPostUpdate.onUpdatePost(post)
        }
    }
    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }
    fun setCallback(callback: ICallbackPostUpdate) {
        icallbackPostUpdate = callback
    }

    override fun getExpandedHeight(): Int {
        return 1000
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding=  BottomSettingBinding.inflate(inflater)
        setUp()
        binding.apply{
            tvedit.setOnClickListener{
                val intent= Intent(context, UpPostActivity::class.java)
                intent.putExtra("post",post)
                resultLauncher.launch(intent)
            }
            tvdelete.setOnClickListener{
                icallbackPostUpdate.onDeletePost(post.id!!)
                this@BottomSettingFragment.dismiss()
            }

            tvreport.setOnClickListener{
                val report= Report()
                report.userid = FirebaseAuth.getInstance().currentUser!!.uid
                report.userID2=post.userid!!
                report.postid=post.id!!
                icallbackPostUpdate.onReportPost(report)
                this@BottomSettingFragment.dismiss()
            }
        }


        return binding.root
    }


    override fun setUp() {
        post = arguments?.getParcelable<Post>("post")!!
        presenter = BottomSettingPresenter(this)
        if(post.userid==FirebaseAuth.getInstance().currentUser!!.uid){
            binding.layoutadmin.visibility=View.VISIBLE
        }
        else{
            binding.layoutadmin.visibility=View.GONE
        }
    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(message: String?) {
        context?.let { Utils.showToast(it, message!!) }
    }

    override fun onFinish() {

    }

}

