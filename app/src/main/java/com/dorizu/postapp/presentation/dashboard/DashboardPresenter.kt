package com.dorizu.postapp.presentation.dashboard

import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val repository: PostRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : DashboardContract.Presenter {

    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private var currentPosts: MutableList<Post> = mutableListOf()

    override fun loadPosts() {
        scope.launch {
            view.showLoading()
            try {
                val posts = repository.getPosts()
                currentPosts = posts.toMutableList()
                if (currentPosts.isEmpty()) {
                    view.showEmpty()
                } else {
                    view.showPosts(currentPosts.toList())
                }
            } catch (e: Exception) {
                view.showError(e.localizedMessage ?: "Terjadi kesalahan saat memuat data")
            } finally {
                view.hideLoading()
            }
        }
    }

    override fun restorePosts(posts: List<Post>) {
        currentPosts = posts.toMutableList()
        view.showPosts(currentPosts.toList())
    }

    override fun toggleBookmark(post: Post) {
        scope.launch {
            val newStatus = repository.toggleBookmark(post)
            val index = currentPosts.indexOfFirst { it.id == post.id }
            if (index != -1) {
                val updated = currentPosts[index].copy(isBookmarked = newStatus)
                currentPosts[index] = updated
                view.updatePostBookmarkStatus(updated)
            }
            view.showBookmarkMessage(newStatus)
        }
    }

    override fun refreshBookmarkStatus() {
        if (currentPosts.isEmpty()) return
        scope.launch {
            try {
                val bookmarkedList = repository.getBookmarks()
                val bookmarkedIds = bookmarkedList.map { it.id }.toSet()

                var hasChanged = false
                currentPosts = currentPosts.map { post ->
                    val shouldBeBookmarked = bookmarkedIds.contains(post.id)
                    if (post.isBookmarked != shouldBeBookmarked) {
                        hasChanged = true
                        post.copy(isBookmarked = shouldBeBookmarked)
                    } else {
                        post
                    }
                }.toMutableList()

                if (hasChanged) {
                    view.showPosts(currentPosts.toList())
                }
            } catch (_: Exception) {
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
