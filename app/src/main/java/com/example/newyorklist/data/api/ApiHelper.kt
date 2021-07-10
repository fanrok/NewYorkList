package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.response.ReviewsResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getEmployees(): Response<ReviewsResponse>
}