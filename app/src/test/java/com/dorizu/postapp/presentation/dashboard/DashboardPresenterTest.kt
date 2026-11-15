package com.dorizu.postapp.presentation.dashboard

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

class DashboardPresenterTest {

    private val view: DashboardContract.View = mock()
    private val repository: PostRepository = mock()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var presenter: DashboardPresenter

    private val dummyPosts = listOf(
        Post(userId = 1, id = 1, title = "Title 1", body = "Body 1", isBookmarked = false),
        Post(userId = 1, id = 2, title = "Title 2", body = "Body 2", isBookmarked = true)
    )

    @Before
    fun setUp() {
        presenter = DashboardPresenter(view, repository, testDispatcher)
    }

    @Test
    fun loadPosts_success_showsPosts() = runTest(testDispatcher) {
        whenever(repository.getPosts()).thenReturn(dummyPosts)

        presenter.loadPosts()

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showPosts(dummyPosts)
        inOrder.verify(view).hideLoading()
    }

    @Test
    fun loadPosts_empty_showsEmpty() = runTest(testDispatcher) {
        whenever(repository.getPosts()).thenReturn(emptyList())

        presenter.loadPosts()

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showEmpty()
        inOrder.verify(view).hideLoading()
    }

    @Test
    fun loadPosts_error_showsError() = runTest(testDispatcher) {
        val errorMessage = "Koneksi internet terputus"
        whenever(repository.getPosts()).thenThrow(RuntimeException(errorMessage))

        presenter.loadPosts()

        val inOrder = inOrder(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showError(errorMessage)
        inOrder.verify(view).hideLoading()
    }

    @Test
    fun toggleBookmark_success_updatesStatusAndShowsMessage() = runTest(testDispatcher) {
        val post = dummyPosts[0]
        whenever(repository.toggleBookmark(post)).thenReturn(true)
        whenever(repository.getPosts()).thenReturn(dummyPosts)

        presenter.loadPosts()
        presenter.toggleBookmark(post)

        verify(view).updatePostBookmarkStatus(post.copy(isBookmarked = true))
        verify(view).showBookmarkMessage(true)
    }
}
