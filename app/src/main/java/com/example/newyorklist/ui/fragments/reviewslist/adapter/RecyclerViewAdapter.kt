package com.example.newyorklist.ui.fragments.reviewslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newyorklist.databinding.ItemLoadingBinding
import com.example.newyorklist.databinding.ItemRowBinding
import com.example.newyorklist.domain.models.Review
import com.squareup.picasso.Picasso


class RecyclerViewAdapter :
    RecyclerView.Adapter<ViewHolder>() {
    private var mItemList: List<Review> = listOf()

    fun setList(list: List<Review>) {
        mItemList = list
        notifyDataSetChanged()
    }

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
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
        if (viewHolder is ItemViewHolder) {
            viewHolder.bind(mItemList[position])
        } else if (viewHolder is LoadingViewHolder) {
            viewHolder.bind()
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mItemList[position].name == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private class ItemViewHolder(bind: ItemRowBinding) : ViewHolder(bind.root) {
        private var binding: ItemRowBinding = bind

        fun bind(review: Review) {
            binding.tvItem.text = review.name
            if (!review.img.isNullOrEmpty()) {
                Picasso.get().load(review.img).into(binding.imageView)
            }
            binding.seeMore.setOnClickListener {
                //TODO тут сделать лямбду
            }
        }
    }

    private class LoadingViewHolder(bind: ItemLoadingBinding) : ViewHolder(bind.root) {
        fun bind() {}
    }


}