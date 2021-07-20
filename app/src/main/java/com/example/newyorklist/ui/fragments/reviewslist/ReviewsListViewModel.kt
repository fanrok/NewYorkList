package com.example.newyorklist.ui.fragments.reviewslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.repositories.ReviewRepository
import com.example.newyorklist.domain.repositories.exceptions.ReviewsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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

    init {
        searchListener()
    }

    private fun searchListener() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searchQuery
                    .map { query ->
                        _listReviews.value = ReviewsListFragmentState.LoadingNew
                        return@map query
                    }
                    .debounce(SEARCH_DELAY_MS)
                    .distinctUntilChanged()
                    .mapLatest { query ->
                        if (query.length >= MIN_QUERY_LENGTH) {
                            reviewRepository.getReviews(query)
                        } else {
                            listOf()
                        }
                    }
                    .collect { data ->
                        if (data.isEmpty()) {
                            _listReviews.value = ReviewsListFragmentState.Empty
                        } else {
                            _listReviews.value = ReviewsListFragmentState.Data(data)
                        }
                    }
            }
        }
    }

    /**
     * Need more reviews
     * Метод осуществляет подгррузку новых данных
     */
    fun needMoreReviews() {
        if (_listReviews.value == ReviewsListFragmentState.LoadingMore) return
        if (_listReviews.value == ReviewsListFragmentState.LoadingNew) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listReviews.value = ReviewsListFragmentState.LoadingMore
                try {
                    val data = reviewRepository.giveMoreReviews()
                    if (data.isEmpty()) {
                        _listReviews.value = ReviewsListFragmentState.NoMoreData
                    } else {
                        _listReviews.value = ReviewsListFragmentState.Data(data)
                    }
                } catch (e: ReviewsException) {
                    _listReviews.value = ReviewsListFragmentState.NoMoreData
                }

            }
        }
    }

    fun setSearchText(s: String) {
        _searchQuery.value = s
    }
}