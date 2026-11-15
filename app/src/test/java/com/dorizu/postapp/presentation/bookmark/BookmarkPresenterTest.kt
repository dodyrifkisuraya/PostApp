package com.dorizu.postapp.presentation.bookmark

import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.repository.PostRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BookmarkPresenterTest {

    private val view: BookmarkContract.View = mock()
    private val repository: PostRepository = mock()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var presenter: BookmarkPresenter

    private val dummyBookmarks = listOf(
        Post(userId = 1, id = 1, title = "Title 1", body = "Body 1", isBookmarked = true),
        Post(userId = 1, id = 2, title = "Title 2", body = "Body 2", isBookmarked = true)
    )

    @Before
    fun setUp() {
        presenter = BookmarkPresenter(view, repository, testDispatcher)
    }

    @Test
    fun loadBookmarks_hasData_showsBookmarks() = runTest(testDispatcher) {
        whenever(repository.getBookmarks()).thenReturn(dummyBookmarks)

        presenter.loadBookmarks()

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showBookmarks(dummyBookmarks)
        inOrder.verify(view).hideLoading()
    }

    @Test
    fun loadBookmarks_empty_showsEmpty() = runTest(testDispatcher) {
        whenever(repository.getBookmarks()).thenReturn(emptyList())

        presenter.loadBookmarks()

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showEmpty()
        inOrder.verify(view).hideLoading()
    }

    @Test
    fun removeBookmark_callsRepositoryAndReloads() = runTest(testDispatcher) {
        val post = dummyBookmarks[0]
        whenever(repository.toggleBookmark(post)).thenReturn(false)
        whenever(repository.getBookmarks()).thenReturn(listOf(dummyBookmarks[1]))

        presenter.removeBookmark(post)

        verify(repository).toggleBookmark(post)
        verify(view).showBookmarkRemovedMessage()
        verify(view).showBookmarks(listOf(dummyBookmarks[1]))
    }
}
