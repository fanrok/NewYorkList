package com.example.newyorklist.ui.fragments.reviewslist

import com.example.newyorklist.domain.repositories.models.Review

sealed class ReviewsListFragmentState {
    object LoadingNew : ReviewsListFragmentState()
    object LoadingMore : ReviewsListFragmentState()
    class Data(val list: List<Review>) : ReviewsListFragmentState()
    object Empty : ReviewsListFragmentState()
}