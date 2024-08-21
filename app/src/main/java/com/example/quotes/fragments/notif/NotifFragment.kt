package com.example.quotes.fragments.notif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotes.Model.Notification
import com.example.quotes.Model.Post
import com.example.quotes.activities.home.HomeActivity
import com.example.quotes.activities.show.ShowActivity
import com.example.quotes.adapters.NotifAdapter
import com.example.quotes.base.BaseFragment
import com.example.quotes.databinding.FragmentNotifBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NotifFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotifFragment : BaseFragment(), INotifView {
    private lateinit var binding: FragmentNotifBinding
    private lateinit var presenter: NotifPresenter
    private lateinit var notifAdapter: NotifAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).iNotifView = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNotifBinding.inflate(layoutInflater)
        setUp()

        presenter.getAllNotif()
        return binding.root
    }

    override fun addNotif(lnotif: MutableList<Notification>) {
        notifAdapter.listnotifications.addAll(lnotif)
        notifAdapter.listnotifications.sortByDescending { it.time }
        notifAdapter.NotifyDataSetChanged()

    }

    override fun showQuotesFragment(post: Post) {
        val intent = Intent(requireContext(), ShowActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }



    override fun setUp() {
        presenter = NotifPresenter(this)
        notifAdapter = NotifAdapter(this, mutableListOf())
        binding.rvnotif.adapter = notifAdapter
        binding.rvnotif.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onFinish() {

    }



}