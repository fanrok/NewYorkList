package com.example.newyorklist.ui.fragments.reviewdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newyorklist.domain.ReviewRepository
import com.example.newyorklist.domain.models.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author Dmitriy Larin
 */
@HiltViewModel
class ReviewDetailViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {
    private val _review =
        MutableStateFlow<Review>(Review(0, "", "", "", ""))
    val review: StateFlow<Review> = _review.asStateFlow()

    fun loadReview(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _review.value = reviewRepository.getRewiewByName(name)
            }
        }
    }
}