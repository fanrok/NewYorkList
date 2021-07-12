package com.example.newyorklist.domain.models

data class Review(
    val Id: Long = 0,
    val Name: String?,
    val Date: String?,
    val Text: String?,
    val Img: String?
)
