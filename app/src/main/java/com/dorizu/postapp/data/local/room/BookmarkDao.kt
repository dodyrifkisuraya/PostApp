package com.dorizu.postapp.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dorizu.postapp.data.local.entity.PostEntity

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarked_posts ORDER BY id DESC")
    suspend fun getAllBookmarks(): List<PostEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_posts WHERE id = :id)")
    suspend fun isBookmarked(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(post: PostEntity)

    @Delete
    suspend fun deleteBookmark(post: PostEntity)

    @Query("DELETE FROM bookmarked_posts WHERE id = :id")
    suspend fun deleteBookmarkById(id: Int)
}
