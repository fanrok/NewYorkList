package com.example.newyorklist.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.newyorklist.data.db.entityes.ReviewEntity

@Dao
interface ReviewDao {
        @Query("SELECT * FROM reviews")
        fun getAll(): List<ReviewEntity>

        @Query("SELECT * FROM reviews WHERE name = :name")
        fun loadByIds(name: String): ReviewEntity

}