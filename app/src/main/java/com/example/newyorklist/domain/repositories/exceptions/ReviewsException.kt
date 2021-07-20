package com.example.newyorklist.domain.repositories.exceptions

/**
 * Reviews exception
 * Исключение для обработки состояния запроса отзывов
 * @constructor
 *
 * @param message - сообщение об исключении
 */
class ReviewsException(message: String) : Exception(message)