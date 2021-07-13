package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkJson
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) :ApiHelper {
    /**
     * @param query - текст, по которому происходит поиск
     * @param offset - с какого номера начинать выборку запроса(выдает по 20 штук)
     */
    override suspend fun getReviews(query: String, offset: Int): Response<NewYorkJson.NewYorkData> =  apiService.getReviews(query, offset)
}