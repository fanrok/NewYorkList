package com.example.newyorklist.data.api.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Review(
    var Id: Long,
    var Name: String?,
    var Date: String?,
    var Text: String?,
    var Img: String?
)
