package com.dorizu.postapp.data.remote

import com.dorizu.postapp.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}