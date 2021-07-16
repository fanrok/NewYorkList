package com.example.newyorklist.data.db.entityes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Dmitriy Larin
 * Review entity
 *
 * @property name
 * @property date
 * @property text
 * @property img
 * @constructor Create empty Review entity
 */
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