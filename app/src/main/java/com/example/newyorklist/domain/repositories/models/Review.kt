package com.example.newyorklist.domain.repositories.models

data class Review(
    val id: Long = 0,
    val name: String = "",
    val date: String = "",
    val text: String = "",
    val img: String = ""
)
