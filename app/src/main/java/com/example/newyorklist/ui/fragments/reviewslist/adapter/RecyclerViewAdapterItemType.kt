package com.example.newyorklist.ui.fragments.reviewslist.adapter

import com.example.newyorklist.domain.repositories.models.Review

/**
 * @author Dmitriy Larin
 */
sealed class RecyclerViewAdapterItemType {
    object Loading : RecyclerViewAdapterItemType()
    class Item(val review: Review) : RecyclerViewAdapterItemType()
}
