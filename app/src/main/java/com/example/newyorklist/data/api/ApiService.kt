package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Dmitriy Larin
 * Api service retrofit
 *
 * @constructor Create empty Api service
 */
interface ApiService {
    companion object {
        const val API_KEY = "fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs"
    }

    @GET("/svc/movies/v2/reviews/search.json")
    suspend fun getReviews(
        @Query("query") query: String,
        @Query("offset") offset: Int,
        @Query("api-key") apiKey: String = API_KEY
    ): Response<NewYorkResponse>
}