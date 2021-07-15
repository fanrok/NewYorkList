package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkJson
import retrofit2.Response

interface ApiHelper {
    /**
     * @param query - поисковый запрос
     * @param offset -  с какой позиции стартовать выборку
     * @return - Ответ от сервера с моделькой
     */
    suspend fun getReviews(query: String, offset: Int): Response<NewYorkJson.NewYorkData>
}