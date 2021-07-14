package com.example.newyorklist.ui.fragments.reviewslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ReviewsListViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {

    companion object {
        private const val SEARCH_DELAY_MS = 500L
    }

    private val _message = MutableStateFlow("Broad")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _listReviews =
        MutableStateFlow<ReviewsListFragmentState>(ReviewsListFragmentState.Empty)
    val listReviews: StateFlow<ReviewsListFragmentState> = _listReviews.asStateFlow()

    @ExperimentalCoroutinesApi
    internal val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    @ExperimentalCoroutinesApi
    internal val internalSearchResult = queryChannel
        .asFlow()
        .debounce(SEARCH_DELAY_MS)
        .mapLatest {
            needMoreReviews()
        }

    private fun needMoreReviews() {
        Log.d("VMTAGS", "needMoreReviews")
    }

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