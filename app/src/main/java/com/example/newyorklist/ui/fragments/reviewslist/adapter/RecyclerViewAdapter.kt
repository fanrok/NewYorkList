package com.example.newyorklist.ui.fragments.reviewslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newyorklist.databinding.ItemLoadingBinding
import com.example.newyorklist.databinding.ItemRowBinding
import com.squareup.picasso.Picasso

/**
 * @param scroll - лямда, дернется когда список будет отмотан до конца
 * @param click - лямбда, дернется при клике на на эдемент
 */
class RecyclerViewAdapter(
    private val scroll: () -> Unit,
    private val click: (name: String) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }


    private var mItemList: List<RecyclerViewAdapterState> = listOf()

    fun setList(list: List<RecyclerViewAdapterState>) {
        val oldList = mItemList
        mItemList = list
        notifyChanges(oldList, list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemRowBinding: ItemRowBinding = ItemRowBinding.inflate(layoutInflater, parent, false)
        val itemLoadingBinding: ItemLoadingBinding =
            ItemLoadingBinding.inflate(layoutInflater, parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            ItemViewHolder(itemRowBinding)
        } else {
            LoadingViewHolder(itemLoadingBinding)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position == mItemList.size - 1) {
            scroll()
        }
        if (viewHolder is ItemViewHolder) {
            viewHolder.bind(mItemList[position] as RecyclerViewAdapterState.Item, click)
        } else if (viewHolder is LoadingViewHolder) {
            viewHolder.bind()
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mItemList[position]) {
            is RecyclerViewAdapterState.Loading -> VIEW_TYPE_LOADING
            is RecyclerViewAdapterState.Item -> VIEW_TYPE_ITEM
        }
    }

    private class ItemViewHolder(bind: ItemRowBinding) : ViewHolder(bind.root) {
        private var binding: ItemRowBinding = bind

        fun bind(review: RecyclerViewAdapterState.Item, click: (name: String) -> Unit) {
            binding.name.text = review.review.name
            if (review.review.img.isNotEmpty()) {
                Picasso.get().load(review.review.img).into(binding.imageView)
            }
            binding.seeMore.setOnClickListener {
                click(review.review.name)
            }
        }
    }

    private class LoadingViewHolder(bind: ItemLoadingBinding) : ViewHolder(bind.root) {
        fun bind() {}
    }

    private fun notifyChanges(
        oldList: List<RecyclerViewAdapterState>,
        newList: List<RecyclerViewAdapterState>
    ) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                if (oldList[oldItemPosition] is RecyclerViewAdapterState.Loading && newList[newItemPosition] is RecyclerViewAdapterState.Loading) return true
                if (oldList[oldItemPosition] is RecyclerViewAdapterState.Item && newList[newItemPosition] is RecyclerViewAdapterState.Loading) return false
                if (oldList[oldItemPosition] is RecyclerViewAdapterState.Loading && newList[newItemPosition] is RecyclerViewAdapterState.Item) return false
                val oldP = oldList[oldItemPosition] as RecyclerViewAdapterState.Item
                val newP = newList[newItemPosition] as RecyclerViewAdapterState.Item
                if (oldP.review.name == newP.review.name) return true
                return false
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })
//TODO починить diff util
        diff.dispatchUpdatesTo(this)
    }

}