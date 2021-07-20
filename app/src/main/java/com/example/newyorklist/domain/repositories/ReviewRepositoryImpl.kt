package com.example.newyorklist.domain.repositories

import com.example.newyorklist.data.api.ApiHelper
import com.example.newyorklist.data.api.models.Result
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.data.db.entityes.ReviewEntity
import com.example.newyorklist.domain.mappers.SimpleMapper
import com.example.newyorklist.domain.repositories.exceptions.ReviewsException
import com.example.newyorklist.domain.repositories.models.Review
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

/**
 * Review repository impl
 *
 * @property reviewDao - дао для работы с бд
 * @property apiHelper - для работы с апи
 * @property mapper - для маппинга данных в ui модель
 * @constructor Create empty Review repository impl
 */
class ReviewRepositoryImpl @Inject constructor(
    private val reviewDao: ReviewDao,
    private val apiHelper: ApiHelper,
    private val mapper: SimpleMapper
) : ReviewRepository {
    private var offset = 0
    private var searchQuery = ""
    private var hasMore = false //флаг, что у сервера есть еще данные
    private val list = mutableListOf<Review>()

    /**
     * Получает список отзывов
     *
     * @param query - поисковый запрос
     * @return - список отзывов
     */
    override suspend fun getReviews(query: String): List<Review> {
        delay(Random.nextLong(0, 5000))//задержка запроса по тз
        list.clear()
        offset = 0
        searchQuery = query
        apiHelper.getReviews(query, offset).let {
            if (it.isSuccessful) {
                it.body()?.let { body ->
                    hasMore = body.has_more
                    if (body.status != "OK") return listOf()
                    if (body.num_results == 0) return listOf()
                    body.results.forEach { item ->
                        list.add(mapper.resultToReview(item))
                        insertToDataBase(item)
                    }
                    return list
                }
            }
        }
        return list
    }

    /**
     * Подгружает, если возможно, еще отзывы
     *
     * @return список отзывов
     */
    override suspend fun giveMoreReviews(): List<Review> {
        if (!hasMore) throw ReviewsException("Больше данных нет")
        delay(Random.nextLong(0, 5000))//задержка запроса по тз
        offset += 20
        apiHelper.getReviews(searchQuery, offset).let {
            if (it.isSuccessful) {
                it.body()?.let { body ->
                    if (body.num_results == 0) return list
                    body.results.forEach { item ->
                        list.add(mapper.resultToReview(item))
                        insertToDataBase(item)
                    }
                    return list
                }
            }
        }
        return list
    }

    /**
     * Ищет в бд отзыв по имени
     *
     * @param name - имя, по которому будет осуществел поиск отзыва
     * @return - возвращает отзыв
     */
    override suspend fun getReviewByName(name: String): Review {
        val review = reviewDao.getByName(name)
        return Review(review.id, review.name, review.date, review.text, review.img)
    }

    private fun insertToDataBase(item: Result) {
        reviewDao.insert(
            ReviewEntity(
                name = item.display_title,
                date = item.publication_date,
                text = item.summary_short,
                img = item.multimedia?.src ?: ""
            )
        )
    }
}