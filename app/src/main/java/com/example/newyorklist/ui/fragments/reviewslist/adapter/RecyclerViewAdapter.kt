package com.example.newyorklist.ui.fragments.reviewslist.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newyorklist.R
import com.example.newyorklist.domain.models.Review
import com.example.newyorklist.oldapp.DetailActivity
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
        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
            ItemViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {
            populateItemRows(viewHolder, position)
        } else if (viewHolder is LoadingViewHolder) {
            showLoadingView(viewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (mItemList[position].name == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) :
        ViewHolder(itemView) {
        var tvItem: TextView? = null
        var imageView: ImageView? = null
        var seeMore: Button? = null

        init {
            tvItem = itemView.findViewById(R.id.tvItem)
            imageView = itemView.findViewById(R.id.imageView)
            seeMore = itemView.findViewById(R.id.seeMore)
        }
    }

    private inner class LoadingViewHolder(itemView: View) :
        ViewHolder(itemView) {
        var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    private fun showLoadingView(
        viewHolder: LoadingViewHolder,
        position: Int
    ) { //ProgressBar would be displayed
    }

    private fun populateItemRows(
        viewHolder: ItemViewHolder,
        position: Int
    ) {
        val item = mItemList[position]
        viewHolder.tvItem?.text = item.name
        if (!item.img.isNullOrEmpty()) {
            Picasso.get().load(item.img).into(viewHolder.imageView)
        }
        viewHolder.seeMore?.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("id", item.id)
            ContextCompat.startActivity(it.context, intent, null)
        }
    }

}