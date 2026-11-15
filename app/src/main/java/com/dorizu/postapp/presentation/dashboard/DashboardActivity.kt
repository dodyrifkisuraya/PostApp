package com.dorizu.postapp.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dorizu.postapp.R
import com.dorizu.postapp.data.local.room.AppDatabase
import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.remote.ApiClient
import com.dorizu.postapp.data.repository.PostRepository
import com.dorizu.postapp.databinding.ActivityDashboardBinding
import com.dorizu.postapp.presentation.bookmark.BookmarkActivity
import com.dorizu.postapp.utils.applySystemBarPadding

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var presenter: DashboardContract.Presenter
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarPadding()

        val username = intent.getStringExtra(EXTRA_USERNAME).orEmpty()
        binding.tvProfileUsername.text = username

        setupRecyclerView()
        setupAction()

        val database = AppDatabase.getDatabase(this)
        val repository = PostRepository(ApiClient.apiService, database.bookmarkDao())
        presenter = DashboardPresenter(this, repository)
        presenter.loadPosts()
    }

    override fun onResume() {
        super.onResume()
        if (::presenter.isInitialized) {
            presenter.refreshBookmarkStatus()
        }
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter { post ->
            presenter.toggleBookmark(post)
        }
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter
    }

    private fun setupAction() {
        binding.btnBookmarkMenu.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showLoading() {
        binding.pbDashboardLoading.visibility = View.VISIBLE
        binding.rvPosts.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.pbDashboardLoading.visibility = View.GONE
    }

    override fun showPosts(posts: List<Post>) {
        binding.rvPosts.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        adapter.submitList(posts)
    }

    override fun showEmpty() {
        binding.rvPosts.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        binding.rvPosts.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvEmptyState.text = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showBookmarkMessage(isBookmarked: Boolean) {
        val msgRes = if (isBookmarked) R.string.bookmark_success else R.string.bookmark_removed
        Toast.makeText(this, getString(msgRes), Toast.LENGTH_SHORT).show()
    }

    override fun updatePostBookmarkStatus(updatedPost: Post) {
        val currentList = adapter.currentList.toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedPost.id }
        if (index != -1) {
            currentList[index] = updatedPost
            adapter.submitList(currentList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}
