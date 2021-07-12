package com.example.newyorklist.ui.fragments.newslist

import com.example.newyorklist.domain.models.Review

sealed class NewsListFragmentState {
    object Loading : NewsListFragmentState()
    class Data(val list: List<Review>) : NewsListFragmentState()
    object Empty : NewsListFragmentState()
}