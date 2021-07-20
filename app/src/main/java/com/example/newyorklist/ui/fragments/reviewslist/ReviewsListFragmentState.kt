package com.example.newyorklist.ui.fragments.reviewslist

import com.example.newyorklist.domain.repositories.models.Review

/**
 * Reviews list fragment state
 * LoadingNew - загрузка новых элементов - пользователь ввел новый поисковый запрос
 * LoadingMore - подгрузка новых элементов - пользователь долистал список до конца
 * NoMoreData - новых данных нет при подгрузке списка при прокрутке
 * Data - пришли новая порция данных (при подгрузке или при запросе)
 * Empty - при запросе новых данных нет
 * @constructor Create empty Reviews list fragment state
 */
sealed class ReviewsListFragmentState {
    object LoadingNew : ReviewsListFragmentState()
    object LoadingMore : ReviewsListFragmentState()
    object NoMoreData : ReviewsListFragmentState()
    class Data(val list: List<Review>) : ReviewsListFragmentState()
    object Empty : ReviewsListFragmentState()
}