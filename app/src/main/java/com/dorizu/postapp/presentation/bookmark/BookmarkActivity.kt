package com.dorizu.postapp.presentation.bookmark

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dorizu.postapp.R
import com.dorizu.postapp.data.local.room.AppDatabase
import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.remote.ApiClient
import com.dorizu.postapp.data.repository.PostRepository
import com.dorizu.postapp.databinding.ActivityBookmarkBinding
import com.dorizu.postapp.presentation.base.BaseSessionActivity
import com.dorizu.postapp.presentation.dashboard.PostAdapter
import com.dorizu.postapp.utils.applySystemBarPadding

class BookmarkActivity : BaseSessionActivity(), BookmarkContract.View {

    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var presenter: BookmarkContract.Presenter
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarPadding()

        val database = AppDatabase.getDatabase(this)
        val repository = PostRepository(ApiClient.apiService, database.bookmarkDao())
        presenter = BookmarkPresenter(this, repository)

        setupRecyclerView()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadBookmarks()
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter { post ->
            presenter.removeBookmark(post)
        }
        binding.rvBookmarks.layoutManager = LinearLayoutManager(this)
        binding.rvBookmarks.adapter = adapter
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun showLoading() {
        binding.pbBookmarkLoading.visibility = View.VISIBLE
        binding.rvBookmarks.visibility = View.GONE
        binding.tvEmptyBookmark.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.pbBookmarkLoading.visibility = View.GONE
    }

    override fun showBookmarks(posts: List<Post>) {
        binding.rvBookmarks.visibility = View.VISIBLE
        binding.tvEmptyBookmark.visibility = View.GONE
        adapter.submitList(posts)
    }

    override fun showEmpty() {
        binding.rvBookmarks.visibility = View.GONE
        binding.tvEmptyBookmark.visibility = View.VISIBLE
    }

    override fun showBookmarkRemovedMessage() {
        Toast.makeText(this, getString(R.string.bookmark_removed), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
