package com.example.newyorklist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.data.db.entityes.ReviewEntity

@Database(entities = [ReviewEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
}