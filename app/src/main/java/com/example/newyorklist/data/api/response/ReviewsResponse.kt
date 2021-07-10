package com.example.newyorklist.data.api.response

import com.example.newyorklist.data.api.models.Review

data class ReviewsResponse(
    val data: List<Review>?=null,
    val status: String?=""
)