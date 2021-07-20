package com.example.newyorklist.ui.fragments.reviewslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newyorklist.databinding.ReviewsListBinding
import com.example.newyorklist.ui.fragments.base.BaseFragmentWithBinding
import com.example.newyorklist.ui.fragments.reviewslist.adapter.RecyclerViewAdapter
import com.example.newyorklist.ui.fragments.reviewslist.adapter.RecyclerViewAdapterItemType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsListFragment : BaseFragmentWithBinding<ReviewsListBinding>() {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private val listForAdapter by lazy { mutableListOf<RecyclerViewAdapterItemType>() }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val viewModel by viewModels<ReviewsListViewModel>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launchWhenStarted {
            viewModel.listReviews.collect {
                seeState(it)
            }
        }

        _binding = ReviewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.search.doAfterTextChanged { editable ->
            lifecycleScope.launch {
                viewModel.setSearchText(editable.toString())
            }
        }
    }

    private fun seeState(state: ReviewsListFragmentState) {
        when (state) {
            is ReviewsListFragmentState.Data -> {
                listForAdapter.clear()
                state.list.forEach {
                    listForAdapter.add(RecyclerViewAdapterItemType.Item(it))
                }
                Log.d("STATE", "data")
            }
            is ReviewsListFragmentState.Empty -> {
                listForAdapter.clear()
                Log.d("STATE", "empty")
            }
            is ReviewsListFragmentState.LoadingNew -> {
                listForAdapter.clear()
                listForAdapter.add(RecyclerViewAdapterItemType.Loading)
                Log.d("STATE", "new")
            }
            is ReviewsListFragmentState.LoadingMore -> {
                listForAdapter.add(RecyclerViewAdapterItemType.Loading)
                Log.d("STATE", "more")
            }
            is ReviewsListFragmentState.NoMoreData -> {
                Toast.makeText(context, "Данных больше нет", Toast.LENGTH_SHORT).show()
                Log.d("STATE", "no more")
            }
        }
        recyclerViewAdapter.setList(listForAdapter)
    }


    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(
            this::scrollListener,
            this::clickListener
        )
        binding.recyclerView.adapter = recyclerViewAdapter
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun scrollListener() {
        Log.d("TAGS", "А вы таки домотали до конца")
        viewModel.needMoreReviews()
    }

    private fun clickListener(id: Long) {
        Log.d("TAGS", "Ты кликнул на $id")
        val action =
            ReviewsListFragmentDirections.actionNewsListFragmentToReviewDetailFragment(id)
        findNavController().navigate(action)
    }


    private fun setText(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_LONG).show()
        Log.d("TAGS", mes)
    }
}
