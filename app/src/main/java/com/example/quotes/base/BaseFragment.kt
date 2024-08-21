package com.example.quotes.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quotes.R
import com.example.quotes.utils.Utils


abstract class BaseFragment : Fragment(),IMVPView {
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    override fun showLoading() {
        (this@BaseFragment.activity as IMVPView).showLoading()
    }

    override fun showError(message: String?) {
        message?.let { this.context?.let { it1 -> Utils.showToast(it1, it) } }
    }

    override fun hideLoading() {
        if(this@BaseFragment.activity!=null) {
            (this@BaseFragment.activity as IMVPView).hideLoading()
        }
    }
}