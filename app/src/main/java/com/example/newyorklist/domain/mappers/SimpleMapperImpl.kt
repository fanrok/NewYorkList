package com.example.newyorklist.domain.mappers

import com.example.newyorklist.data.api.models.Result
import com.example.newyorklist.domain.repositories.models.Review
import javax.inject.Inject

/**
 * @author Dmitriy Larin
 * * Simple mapper
 *
 * @constructor Create empty Simple mapper
 */
class SimpleMapperImpl @Inject constructor() : SimpleMapper {
    override fun resultToReview(result: Result): Review {
        return Review(
            name = result.display_title,
            date = result.publication_date,
            text = result.summary_short,
            img = result.multimedia?.src ?: "",
            link = result.link.url
        )
    }
}