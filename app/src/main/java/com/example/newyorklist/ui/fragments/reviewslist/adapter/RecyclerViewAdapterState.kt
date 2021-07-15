package com.example.newyorklist.ui.fragments.reviewslist.adapter

import com.example.newyorklist.domain.repositories.models.Review

/**
 * @author Dmitriy Larin
 */
sealed class RecyclerViewAdapterState {
    object Loading : RecyclerViewAdapterState()
    class Item(val review: Review) : RecyclerViewAdapterState()
}
