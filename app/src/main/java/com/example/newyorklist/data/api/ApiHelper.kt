package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkResponse
import retrofit2.Response

/**
 * @author Dmitriy Larin
 * Api helper
 *
 * @constructor Create empty Api helper
 */
interface ApiHelper {
    /**
     * @param query - поисковый запрос
     * @param offset -  с какой позиции стартовать выборку
     * @return - Ответ от сервера с моделькой
     */
    suspend fun getReviews(query: String, offset: Int): Response<NewYorkResponse>
}