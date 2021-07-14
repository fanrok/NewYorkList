package com.example.newyorklist.ui.fragments.reviewslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewsListViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {

    private val _message = MutableStateFlow("Broad")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _listReviews =
        MutableStateFlow<ReviewsListFragmentState>(ReviewsListFragmentState.Empty)
    val listReviews: StateFlow<ReviewsListFragmentState> = _listReviews.asStateFlow()

    private var haveMoreReviews = false
    private var offset = 0
    private var query = ""

    init {
        setMessage("init")
        loadData()
    }

    fun setMessage(s: String) {
        _message.value = s
    }

    private fun loadData() {
        _listReviews.value = ReviewsListFragmentState.Loading
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                reviewRepository.getReviews("q", 0)
            }
            if (data.isEmpty()) {
                _listReviews.value = ReviewsListFragmentState.Empty
            } else {
                _listReviews.value = ReviewsListFragmentState.Data(data)
            }
        }
    }
}