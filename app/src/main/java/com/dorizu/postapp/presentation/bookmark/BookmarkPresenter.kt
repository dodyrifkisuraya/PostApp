package com.dorizu.postapp.presentation.bookmark

import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BookmarkPresenter(
    private val view: BookmarkContract.View,
    private val repository: PostRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BookmarkContract.Presenter {

    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    override fun loadBookmarks() {
        scope.launch {
            view.showLoading()
            try {
                val bookmarks = repository.getBookmarks()
                if (bookmarks.isEmpty()) {
                    view.showEmpty()
                } else {
                    view.showBookmarks(bookmarks)
                }
            } finally {
                view.hideLoading()
            }
        }
    }

    override fun removeBookmark(post: Post) {
        scope.launch {
            repository.toggleBookmark(post)
            view.showBookmarkRemovedMessage()
            loadBookmarks()
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
