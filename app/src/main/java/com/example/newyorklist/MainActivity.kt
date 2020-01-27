package com.example.newyorklist

//import android.R

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/*
кхм...итак здрасте. В прошлый раз попытка выполнить тестовый провалилась. Я уперся в то, что не смог сделать автоподгрузку в RecyclerView. И в попытках ее прикрутить превратил приложение в полное гавно. куча разных модулей, куча закомментированного кода...треш короче. Это очередная попытка сделать это. Начнем от противного - сделать таки автоподгрузку. И я ее таки сделал!
 */

class MainActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<String?> = ArrayList()

    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        populateData()
        initAdapter()
        initScrollListener()
    }

    private fun populateData() {
        var i = 0
        while (i < 10) {
            rowsArrayList.add("Item $i")
            i++
        }
    }

    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
        recyclerView!!.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) { //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(null)

        recyclerView!!.post {
            // There is no need to use notifyDataSetChanged()
            recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        }

//        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                rowsArrayList.add("Item $currentSize")
                currentSize++
            }
            recyclerViewAdapter!!.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }
}
