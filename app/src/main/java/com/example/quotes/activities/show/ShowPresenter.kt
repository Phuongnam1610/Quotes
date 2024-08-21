package com.example.quotes.activities.show

import com.example.quotes.Model.Post
import com.example.quotes.Model.Report
import com.example.quotes.repository.PostRepository
import com.example.quotes.repository.ReportRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowPresenter(val view: IShowView) {
    fun loadPost(post: Post) {
        view.showLoading()
        view.displayPost(post!!)
        view.hideLoading()
    }

    fun deletePost(postID: String) {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val c = PostRepository().deletePostByPostID(postID)
            withContext(Dispatchers.Main) {
                if (c) {
                    view.showError("Đã xóa thành công bài viết!")
                    view.hideLoading()
                    view.onFinish()
                } else {
                    view.showError("Có lỗi xảy ra khi xóa bài viết!")
                    view.hideLoading()
                }
            }
        }

    }

    fun reportPost(report: Report) {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val c = ReportRepository().addReport(report)
            withContext(Dispatchers.Main) {
                if (c) {
                    view.showError("Đã tố cáo thành công!")
                    view.hideLoading()
                    view.onFinish()
                } else {
                    view.showError("Có lỗi khi xảy ra tố cáo!")
                    view.hideLoading()
                }
            }
        }
    }


}