package com.example.newyorklist.domain

import com.example.newyorklist.data.db.dao.ReviewDao
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val reviewDao: ReviewDao):ReviewRepository {
}