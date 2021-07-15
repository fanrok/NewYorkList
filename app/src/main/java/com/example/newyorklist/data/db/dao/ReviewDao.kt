package com.example.newyorklist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newyorklist.data.db.entityes.ReviewEntity


@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE name = :name")
    fun getByName(name: String): ReviewEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(employee: ReviewEntity)

}