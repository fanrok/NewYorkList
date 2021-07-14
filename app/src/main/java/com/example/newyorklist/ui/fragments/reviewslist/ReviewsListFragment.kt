package com.example.newyorklist.ui.fragments.reviewslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.newyorklist.R
import com.example.newyorklist.ui.fragments.reviewslist.adapter.RecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.news_list_fragment.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReviewsListFragment : Fragment() {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    var isLoading = false

    private val viewModel by viewModels<ReviewsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launchWhenStarted {
            viewModel.listReviews.collect {
                seeState(it)
            }
        }

//        initScrollListener()
//        if (listReviews.size > 0) {//значение больше нуля значит мы восстановили стейт. промотаем список до нужной позиции (smoothScrollToPosition не работает)
//            recyclerView?.scrollToPosition(StateSave.scrollPosition)
//        }
//
//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                data.setQuery(query)
//                listReviews.clear()
//                loadMore(true)
//                return false
//            }
//
//            //TODO: мне не нравится как это работает. нужно решение получше
//            override fun onQueryTextChange(newText: String): Boolean {
//                val handler = Handler()
//                handler.removeCallbacksAndMessages(null)
//                handler.postDelayed({
//                    data.setQuery(newText)
//                    listReviews.clear()
//                    loadMore(true)
//                }, 1000)
//                return false
//            }
//        })
        return inflater.inflate(R.layout.news_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun seeState(state: ReviewsListFragmentState) {
        when (state) {
            is ReviewsListFragmentState.Data -> {
//                setText(state.list.toString())
                recyclerViewAdapter.setList(state.list)
            }
            is ReviewsListFragmentState.Empty -> {
                setText("Empty")
            }
            is ReviewsListFragmentState.Loading -> {
                setText("Loading")
            }
        }
    }


    //
    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter
    }

//    private fun initScrollListener() {
//        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
//                StateSave.scrollPosition =
//                    linearLayoutManager.findFirstCompletelyVisibleItemPosition()
//                if (!isLoading && data.getHasMore()) {
//                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == listReviews.size - 1 && listReviews.size > 1) {
//                        loadMore(false)
//                        isLoading = true
//                    }
//                }
//            }
//        })
//    }
//
//    private fun loadMore(newList: Boolean = false) {
//        if (newList) {
//            recyclerView?.post {
//                recyclerViewAdapter?.notifyDataSetChanged()
//            }
//        }
//        listReviews.add(Review())
//        recyclerView?.post {
//            recyclerViewAdapter?.notifyItemInserted(listReviews.size)
//        }
//        val scrollPosition: Int = listReviews.size
//        val handler = Handler()
//        handler.postDelayed({
//            ioScope.launch {
//                val result = data.loadData()
//                if (newList) {
//                    listReviews.clear()
//                } else {
//                    listReviews.removeAt(listReviews.size - 1)
//                }
//                val reviews = db.addReviewsInDB(db, result.results, listReviews)
//                uiScope.launch {
//                    listReviews = reviews
//                    recyclerView?.post {
//                        recyclerViewAdapter?.notifyItemRangeChanged(
//                            scrollPosition - 1,
//                            listReviews.size - scrollPosition
//                        )
//                    }
//                    isLoading = false
//                }
//            }
//        }, 0)
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        val search: SearchView = findViewById(R.id.search)
//        val searchText = search.query
//        outState.putCharSequence("searchText", searchText)
//        StateSave.reviews = listReviews
//
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {//я не понимаю почему, но при поворотах экрана и при изменении экрана этот метод просто не вызывается
//        super.onRestoreInstanceState(savedInstanceState)
//        val userText = savedInstanceState.getCharSequence("searchText")
//        val search: SearchView = findViewById(R.id.search)
//        search.setQuery(userText, false)
////        listReviews = StateSave.reviews
//    }

//    override fun onPause() {
//        super.onPause()
//        StateSave.reviews = listReviews
//    }
//
//    override fun onResume() {
//        super.onResume()
//    }

    private fun setText(mes: String) {
        message.text = mes
    }
}