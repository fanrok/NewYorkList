package com.example.newyorklist.ui.fragments.reviewdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newyorklist.databinding.ReviewDetailBinding
import com.example.newyorklist.domain.models.Review
import com.example.newyorklist.ui.fragments.base.BaseFragmentWithBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * @author Dmitriy Larin
 */
@AndroidEntryPoint
class ReviewDetailFragment : BaseFragmentWithBinding<ReviewDetailBinding>() {

    private lateinit var nameOfReview: String
    private val viewModel by viewModels<ReviewDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReviewDetailBinding.inflate(inflater, container, false)
        if (arguments != null) {
            nameOfReview =
                ReviewDetailFragmentArgs.fromBundle(requireArguments()).nameOfReview.toString()
            if (nameOfReview.isBlank() || nameOfReview.isEmpty()) {
                findNavController().popBackStack()
            }
        } else {
            findNavController().popBackStack()
        }

        viewModel.loadReview(nameOfReview)

        lifecycleScope.launchWhenStarted {
            viewModel.review.collect {
                setView(it)
            }
        }

        return binding.root
    }

    private fun setView(review: Review) {

        binding.detailText.text = review.text
        binding.name.text = review.name
        binding.date.text = review.date
        if (review.img.isNotEmpty()) {
            Picasso.get().load(review.img).into(binding.detailImage)
        }

        binding.goBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}