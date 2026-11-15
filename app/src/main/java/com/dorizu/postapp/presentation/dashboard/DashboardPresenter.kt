package com.dorizu.postapp.presentation.dashboard

import com.dorizu.postapp.data.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val repository: PostRepository
) : DashboardContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun loadPosts() {
        scope.launch {
            view.showLoading()
            try {
                val posts = repository.getPosts()
                if (posts.isEmpty()) {
                    view.showEmpty()
                } else {
                    view.showPosts(posts)
                }
            } catch (e: Exception) {
                view.showError(e.localizedMessage ?: "Terjadi kesalahan saat memuat data")
            } finally {
                view.hideLoading()
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
