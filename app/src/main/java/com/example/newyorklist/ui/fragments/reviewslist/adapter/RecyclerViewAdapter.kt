package com.example.newyorklist.ui.fragments.reviewslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newyorklist.databinding.ItemLoadingBinding
import com.example.newyorklist.databinding.ItemRowBinding
import com.example.newyorklist.domain.repositories.models.Review
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


    private var mItemList: List<Review> = listOf()

    fun setList(list: List<Review>) {
        mItemList = list
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
            viewHolder.bind(mItemList[position], click)
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

        fun bind(review: Review, click: (name: String) -> Unit) {
            binding.name.text = review.name
            if (!review.img.isNullOrEmpty()) {
                Picasso.get().load(review.img).into(binding.imageView)
            }
            binding.seeMore.setOnClickListener {
                click(review.name)
            }
        }
    }

    private class LoadingViewHolder(bind: ItemLoadingBinding) : ViewHolder(bind.root) {
        fun bind() {}
    }

}