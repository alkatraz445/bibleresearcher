package com.mandk.biblereasercher.utils

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarksDao(): BookmarkDao
}