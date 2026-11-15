package com.dorizu.postapp.data.repository

import com.dorizu.postapp.data.local.entity.PostEntity
import com.dorizu.postapp.data.local.room.BookmarkDao
import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(
    private val apiService: ApiService,
    private val bookmarkDao: BookmarkDao
) {

    suspend fun getPosts(): List<Post> {
        return withContext(Dispatchers.IO) {
            val remotePosts = apiService.getPosts()
            val bookmarkedIds = bookmarkDao.getAllBookmarks().map { it.id }.toSet()

            remotePosts.map { post ->
                post.copy(isBookmarked = bookmarkedIds.contains(post.id))
            }
        }
    }

    suspend fun getBookmarks(): List<Post> {
        return withContext(Dispatchers.IO) {
            bookmarkDao.getAllBookmarks().map { it.toPost() }
        }
    }

    suspend fun toggleBookmark(post: Post): Boolean {
        return withContext(Dispatchers.IO) {
            val isCurrentlyBookmarked = bookmarkDao.isBookmarked(post.id)
            if (isCurrentlyBookmarked) {
                bookmarkDao.deleteBookmarkById(post.id)
                false
            } else {
                bookmarkDao.insertBookmark(PostEntity.fromPost(post))
                true
            }
        }
    }
}
