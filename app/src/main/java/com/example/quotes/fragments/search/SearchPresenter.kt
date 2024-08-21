package com.example.quotes.fragments.search

import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPresenter(val view: ISearchView) {
    private var postRepository: PostRepository = PostRepository()

    fun searchPost(quotes: String, idcategory: String) {
        view.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            val listposts = postRepository.searchQuotesByContentCategory(quotes, idcategory)
            withContext(Dispatchers.Main) {
                view.displayPostList(listposts)
                view.hideLoading()
            }

        }


    }

    fun loadCategories() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val list = CategoryRepository().loadCategories()
            withContext(Dispatchers.Main) {
                view.setCategories(list)
                view.hideLoading()
            }
        }
    }

}