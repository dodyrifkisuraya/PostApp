package com.dorizu.postapp.presentation.bookmark

import com.dorizu.postapp.data.model.Post

interface BookmarkContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showBookmarks(posts: List<Post>)
        fun showEmpty()
        fun showBookmarkRemovedMessage()
    }

    interface Presenter {
        fun loadBookmarks()
        fun removeBookmark(post: Post)
        fun onDestroy()
    }
}
