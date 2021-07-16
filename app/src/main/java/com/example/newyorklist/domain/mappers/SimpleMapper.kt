package com.example.newyorklist.domain.mappers

import com.example.newyorklist.data.api.models.Result
import com.example.newyorklist.domain.repositories.models.Review

/**
 * @author Dmitriy Larin
 */
interface SimpleMapper {
    /**
     * Мапит модельку Result из ответа сервера в модельку Review
     *
     * @param result - моделька сервера в ответе пользователя
     * @return - моделька отзыва для ui
     */
    fun resultToReview(result: Result): Review
}