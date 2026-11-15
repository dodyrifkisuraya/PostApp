package com.dorizu.postapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable {
    val imageUrl: String
        get() = "https://picsum.photos/id/$id/200/200"
}
