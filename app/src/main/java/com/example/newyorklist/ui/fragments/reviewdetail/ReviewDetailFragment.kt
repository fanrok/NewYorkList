package com.example.newyorklist.ui.fragments.reviewdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newyorklist.databinding.ReviewDetailBinding
import com.example.newyorklist.ui.fragments.base.BaseFragmentWithBinding

/**
 * @author Dmitriy Larin
 */
class ReviewDetailFragment : BaseFragmentWithBinding<ReviewDetailBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReviewDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}