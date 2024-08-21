package com.example.quotes.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.adapters.PostVPAdapter
import com.example.quotes.base.BaseFragment
import com.example.quotes.databinding.FragmentHomeBinding



class HomeFragment : BaseFragment(), IHomeView {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var presenter: HomePresenter


    private lateinit var postadapter: PostVPAdapter
    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE

    }

    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
    }

    override fun onFinish() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setUp()
        binding.apply {
            btnSearch.setOnClickListener {
                showFragmentSearch()
            }
            viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        val currentItem = binding.viewpager.currentItem
                        val totalItems = binding.viewpager.adapter!!.itemCount
                        if (currentItem == 0) {
                            presenter.loadPostViewPager()
                            binding.viewpager.setCurrentItem(0, false);

                        } else if (currentItem == totalItems - 1) {
                            presenter.loadPostViewPager()
                            binding.viewpager.setCurrentItem(0, false);

                        }
                    }
                }
            })

        }


        return binding.root
    }


    override fun displayPostList(posts: List<Post>) {
        postadapter.addPostToList(posts)
    }

    override fun scrollToPosition(index: Int) {
        binding.viewpager.currentItem=index
    }

    override fun refreshListPost() {
        presenter.loadPostViewPager()
    }

    override fun setUp() {
        postadapter = PostVPAdapter(this, mutableListOf())
        binding.viewpager.adapter = postadapter
        presenter = HomePresenter(this)
        presenter.loadPostViewPager()
    }


    private fun showFragmentSearch() {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        findNavController().navigate(action)
    }





}