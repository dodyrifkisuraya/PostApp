package com.dorizu.postapp.data.repository

import com.dorizu.postapp.data.model.Post
import com.dorizu.postapp.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(
    private val apiService: ApiService
) {

    suspend fun getPosts(): List<Post> {
        return withContext(Dispatchers.IO) {
            apiService.getPosts()
        }
    }
}
