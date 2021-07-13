package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkJson
import retrofit2.Response

interface ApiHelper {
    suspend fun getReviews(query: String, offset: Int): Response<NewYorkJson.NewYorkData>
}