package com.example.newyorklist.domain

import com.example.newyorklist.domain.models.Review


interface ReviewRepository {
    /**
     * @param query - поисковый запрос
     * @param offset - с какой позиции запрашивать выборку
     * @return список отзывов
     */
    suspend fun getReviews(query: String, offset: Int): List<Review>

    /**
     * @param name - имя заголовка. Бек не присылает уникальный id, поэтому выборку делаю по имени
     * @return отзыв
     */
    suspend fun getRewiewByName(name: String): Review
}