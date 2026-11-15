package com.dorizu.postapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dorizu.postapp.data.model.Post

@Entity(tableName = "bookmarked_posts")
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) {
    fun toPost(): Post {
        return Post(
            userId = userId,
            id = id,
            title = title,
            body = body,
            isBookmarked = true
        )
    }

    companion object {
        fun fromPost(post: Post): PostEntity {
            return PostEntity(
                id = post.id,
                userId = post.userId,
                title = post.title,
                body = post.body
            )
        }
    }
}
