package com.example.newyorklist.domain

import com.example.newyorklist.data.api.ApiHelper
import com.example.newyorklist.data.api.models.NewYorkJson
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.domain.models.Review
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDao: ReviewDao,
    private val apiHelper: ApiHelper
) : ReviewRepository {
    override suspend fun getReviews(query: String, offset: Int): List<Review> {
        val mapper = SimpleMapper()
        val list = mutableListOf<Review>()
        apiHelper.getReviews(query, offset).let {
            if (it.isSuccessful) {
                it.body()?.let { body ->
                    body.results.forEach { item ->
                        list.add(mapper.resultToReview(item))
                    }
                    return list
                }
            }
        }
        return list
    }

    override suspend fun getReviewByNameFromName(name: String): Review {
        return Review(0, "1", "", "", "")
    }

    class SimpleMapper {
        fun resultToReview(result: NewYorkJson.Result): Review {
            return Review(
                Name = result.display_title,
                Date = result.publication_date,
                Text = result.headline,
                Img = result.multimedia?.src ?: ""
            )
        }
    }
}