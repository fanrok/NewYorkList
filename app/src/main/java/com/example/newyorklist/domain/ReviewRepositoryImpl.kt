package com.example.newyorklist.domain

import com.example.newyorklist.data.api.ApiHelper
import com.example.newyorklist.data.api.models.NewYorkJson
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.data.db.entityes.ReviewEntity
import com.example.newyorklist.domain.models.Review
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDao: ReviewDao,
    private val apiHelper: ApiHelper
) : ReviewRepository {
    private var offset = 0
    private var searchQuery = ""
    private val list = mutableListOf<Review>()
    private val mapper = SimpleMapper()

    override suspend fun getReviews(query: String): List<Review> {
        list.clear()
        offset = 0
        searchQuery = query
        apiHelper.getReviews(query, offset).let {
            if (it.isSuccessful) {
                it.body()?.let { body ->
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

    override suspend fun giveMoreReviews(): List<Review> {
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

    override suspend fun getRewiewByName(name: String): Review {
        val review = reviewDao.getByName(name)
        return Review(review.id, review.name, review.date, review.text, review.img)
    }

    private fun insertToDataBase(item: NewYorkJson.Result) {
        reviewDao.insert(
            ReviewEntity(
                name = item.display_title,
                date = item.publication_date,
                text = item.summary_short,
                img = item.multimedia?.src ?: ""
            )
        )
    }

    class SimpleMapper {
        fun resultToReview(result: NewYorkJson.Result): Review {
            return Review(
                name = result.display_title,
                date = result.publication_date,
                text = result.summary_short,
                img = result.multimedia?.src ?: ""
            )
        }
    }
}