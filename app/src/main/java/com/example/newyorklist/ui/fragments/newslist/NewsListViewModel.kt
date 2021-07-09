package com.example.newyorklist.ui.fragments.newslist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor() : ViewModel() {

    private val _message = MutableStateFlow("Broad")
    val message: StateFlow<String> = _message.asStateFlow()

    init {
        setMessage("init")
    }

    fun setMessage(s: String) {
        _message.value = s
    }
}