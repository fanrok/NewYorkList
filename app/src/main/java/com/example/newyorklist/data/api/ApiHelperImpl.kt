package com.example.newyorklist.data.api

import com.example.newyorklist.data.api.models.NewYorkResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Dmitriy Larin
 * Api helper impl
 *
 * @property apiService
 * @constructor Create empty Api helper impl
 */
class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    /**
     * @param query - текст, по которому происходит поиск
     * @param offset - с какого номера начинать выборку запроса(выдает по 20 штук)
     * @return - ответ от сервера
     */
    override suspend fun getReviews(query: String, offset: Int): Response<NewYorkResponse> =
        apiService.getReviews(query, offset)
}