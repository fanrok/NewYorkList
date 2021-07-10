package com.example.newyorklist.data.db.entityes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey var Id: Long,
    @ColumnInfo(name = "name") var Name: String?,
    @ColumnInfo(name = "date") var Date: String?,
    @ColumnInfo(name = "text") var Text: String?,
    @ColumnInfo(name = "img") var Img: String?
)