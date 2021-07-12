package com.example.newyorklist.domain

import com.example.newyorklist.domain.models.Review


interface ReviewRepository {
    suspend fun getReviews(query:String, offset:Int):List<Review>
    suspend fun getReviewByNameFromName(name:String):Review
}