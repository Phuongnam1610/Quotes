package com.example.quotes.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotes.Model.Category
import com.example.quotes.Model.Post
import com.example.quotes.R
import com.example.quotes.adapters.PostItemAdapter
import com.example.quotes.base.BaseFragment
import com.example.quotes.databinding.FragmentSearchBinding


class SearchFragment : BaseFragment(), ISearchView {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: PostItemAdapter
    private lateinit var presenter: SearchPresenter
    private var categories: MutableList<Category> = mutableListOf()
    private var first: Boolean = true
    override fun showLoading() {
        binding.animationView.visibility = View.VISIBLE
        binding.layoutmain.visibility = View.GONE
    }

    override fun onFinish() {

    }

    override fun hideLoading() {
        binding.animationView.visibility = View.GONE
        binding.layoutmain.visibility = View.VISIBLE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUp()
        binding.btnsearch.setOnClickListener {
            presenter.searchPost(
                binding.edtsearch.text.toString(),
                categories[binding.spinner.selectedItemPosition].id
            )
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                presenter.searchPost(
                    binding.edtsearch.text.toString(),
                    categories[binding.spinner.selectedItemPosition].id
                )

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (first) {
            first = false
        } else {
            if (categories?.size != 0) {
                presenter.searchPost(
                    binding.edtsearch.text.toString(),
                    categories[binding.spinner.selectedItemPosition].id
                )
            }
        }
    }

    override fun setCategories(listCategories: MutableList<Category>) {
        categories = listCategories
        categories.add(Category("All", "All"))
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            categories.map { it.name }
        )
        adapter.setDropDownViewResource(R.layout.item_spinner)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(binding.spinner.count - 1)
        searchAdapter = PostItemAdapter(categories,this, mutableListOf())
        binding.rvsearch.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvsearch.adapter = searchAdapter

    }

    override fun displayPostList(posts: MutableList<Post>) {
        searchAdapter.addPostToList(posts)
    }

    override fun showQuotesActivity(post: Post) {
        val bundle = Bundle().apply {
            putParcelable("post", post)
        }
        findNavController().navigate(R.id.action_searchFragment_to_showActivity, bundle)
    }

    override fun setUp() {
        presenter = SearchPresenter(this)
        presenter.loadCategories()


//        if(categories?.size!! >0){
//            presenter.searchPost(
//                binding.edtsearch.text.toString(),
//                categories[binding.spinner.selectedItemPosition].id
//            )
//        }
    }


}