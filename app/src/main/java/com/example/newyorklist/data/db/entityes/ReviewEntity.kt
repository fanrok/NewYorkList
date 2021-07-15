package com.example.newyorklist.data.db.entityes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "img") var img: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}