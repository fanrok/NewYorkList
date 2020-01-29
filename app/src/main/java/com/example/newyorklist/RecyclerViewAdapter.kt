package com.example.newyorklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newyorklist.data.Review
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(var mItemList: List<Review>) :
    RecyclerView.Adapter<ViewHolder>() {
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
        return if (mItemList[position].Name == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) :
        ViewHolder(itemView) {
        var tvItem: TextView? = null
        var imageView: ImageView? = null

        init {
            tvItem = itemView.findViewById(R.id.tvItem)
            imageView = itemView.findViewById(R.id.imageView)
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
        val item = mItemList!![position]
        viewHolder.tvItem?.text = item.Name
        if (!item.Img.isNullOrEmpty()) {
            Picasso.get().load(item.Img).into(viewHolder.imageView)

        }
    }

}