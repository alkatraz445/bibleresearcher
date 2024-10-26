package com.mandk.biblereasercher.utils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val translationName: String?,
    val translationUrl: String?,
    val testament: String?,
    val bookAbbrName : String?,
    val bookUrl : String?,
    val chapter: String?,
//    val scrollState: ScrollState
)

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAll(): Flow<List<Bookmark>>

    @Query("SELECT COUNT(*) FROM bookmark")
    fun getBookmarkCount() : Flow<Int>

    @Query("SELECT * FROM bookmark WHERE id == :bookmarkId")
    suspend fun loadById(bookmarkId: Int): Bookmark

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg users: Bookmark)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(value : Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

}