package com.example.newyorklist.ui.fragments.reviewslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.repositories.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.random.Random

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class ReviewsListViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {

    companion object {
        private const val SEARCH_DELAY_MS = 500L
        private const val MIN_QUERY_LENGTH = 1
    }

    private val _searchQuery = MutableStateFlow("")

    private val _listReviews =
        MutableStateFlow<ReviewsListFragmentState>(ReviewsListFragmentState.Empty)
    val listReviews: StateFlow<ReviewsListFragmentState> = _listReviews.asStateFlow()

    private var haveMoreReviews = false
    private var offset = 0
    private var query = ""

    init {
        searchListener()
    }

    private fun searchListener() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searchQuery
                    .debounce(SEARCH_DELAY_MS)
                    .distinctUntilChanged()
                    .collect {
                        Log.d("RLVM", it)
                        if (it.length >= MIN_QUERY_LENGTH) {
                            _listReviews.value = ReviewsListFragmentState.Loading
                            delay(Random.nextLong(0, 5000))//задержка запроса по тз
                            val data = reviewRepository.getReviews(it)
                            if (data.isEmpty()) {
                                _listReviews.value = ReviewsListFragmentState.Empty
                            } else {
                                _listReviews.value = ReviewsListFragmentState.Data(data)
                            }
                        }
                    }
            }
        }
    }

    fun needMoreReviews() {
        if (_listReviews.value == ReviewsListFragmentState.Loading) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listReviews.value = ReviewsListFragmentState.Loading
                delay(Random.nextLong(0, 5000))//задержка запроса по тз
                val data = reviewRepository.giveMoreReviews()
                if (data.isEmpty()) {
                    _listReviews.value = ReviewsListFragmentState.Empty
                } else {
                    _listReviews.value = ReviewsListFragmentState.Data(data)
                }
            }
        }
    }

    fun setSearchText(s: String) {
        _searchQuery.value = s
    }
}