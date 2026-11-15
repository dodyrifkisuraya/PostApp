package com.dorizu.postapp.presentation.dashboard

import com.dorizu.postapp.data.model.Post

interface DashboardContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showPosts(posts: List<Post>)
        fun showEmpty()
        fun showError(message: String)
        fun showBookmarkMessage(isBookmarked: Boolean)
        fun updatePostBookmarkStatus(updatedPost: Post)
    }

    interface Presenter {
        fun loadPosts()
        fun restorePosts(posts: List<Post>)
        fun toggleBookmark(post: Post)
        fun refreshBookmarkStatus()
        fun onDestroy()
    }
}
