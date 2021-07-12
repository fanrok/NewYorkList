package com.example.newyorklist.ui.fragments.newslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.ReviewRepository
import com.example.newyorklist.domain.models.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(private val reviewRepository: ReviewRepository) : ViewModel() {

    private val _message = MutableStateFlow("Broad")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _listReviews = MutableSharedFlow<List<Review>>()
    val listReviews: SharedFlow<List<Review>> = _listReviews.asSharedFlow()

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

    private fun loadData(){
        viewModelScope.launch {
            _listReviews.tryEmit(reviewRepository.getReviews("q", 0))
        }
    }
}