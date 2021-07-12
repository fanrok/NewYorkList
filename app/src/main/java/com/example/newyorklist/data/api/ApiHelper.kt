package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkJson
import com.example.newyorklist.data.api.response.ReviewsResponse
import retrofit2.Response
import java.time.ZoneOffset

interface ApiHelper {
    suspend fun getReviews(query: String, offset: Int): Response<NewYorkJson.NewYorkData>
}