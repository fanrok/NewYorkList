package com.example.newyorklist.ui.fragments.reviewslist

import com.example.newyorklist.domain.models.Review

sealed class ReviewsListFragmentState {
    object Loading : ReviewsListFragmentState()
    class Data(val list: List<Review>) : ReviewsListFragmentState()
    object Empty : ReviewsListFragmentState()
}