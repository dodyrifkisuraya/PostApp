package com.dorizu.postapp.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    val isBookmarked: Boolean = false
) {
    val imageUrl: String
        get() = "https://picsum.photos/id/$id/200/200"
}
