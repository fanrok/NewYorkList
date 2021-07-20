package com.example.newyorklist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newyorklist.data.db.entityes.ReviewEntity

/**
 * @author Dmitriy Larin
 * Review dao
 *
 * @constructor Create empty Review dao
 */
@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE name = :name")
    fun getByName(name: String): ReviewEntity

    @Query("SELECT * FROM reviews WHERE id = :id")
    fun getById(id: Long): ReviewEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: ReviewEntity): Long

}