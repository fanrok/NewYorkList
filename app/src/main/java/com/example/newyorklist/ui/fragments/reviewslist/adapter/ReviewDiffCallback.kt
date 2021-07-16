package com.example.newyorklist.ui.fragments.reviewslist.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Dmitriy Larin
 */
class ReviewDiffCallback(
    private val oldList: List<RecyclerViewAdapterItemType>,
    private val newList: List<RecyclerViewAdapterItemType>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition] is RecyclerViewAdapterItemType.Loading && newList[newItemPosition] is RecyclerViewAdapterItemType.Loading) return true
        if (oldList[oldItemPosition] is RecyclerViewAdapterItemType.Item && newList[newItemPosition] is RecyclerViewAdapterItemType.Loading) return false
        if (oldList[oldItemPosition] is RecyclerViewAdapterItemType.Loading && newList[newItemPosition] is RecyclerViewAdapterItemType.Item) return false
        val oldP = oldList[oldItemPosition] as RecyclerViewAdapterItemType.Item
        val newP = newList[newItemPosition] as RecyclerViewAdapterItemType.Item
        if (oldP.review.name == newP.review.name) return true
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

}