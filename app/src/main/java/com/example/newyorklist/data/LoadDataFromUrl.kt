package com.example.newyorklist.data

import android.util.Log
import com.google.gson.Gson
//import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * api-key: fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs
secret: HTfeigAHETiypjDE
api-id: efb5ab20-6535-45da-92d3-694955cef14c
https://api.nytimes.com/svc/movies/v2/reviews/search.json?query=godfather&api-key=fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs
 */
class LoadDataFromUrl {
    private val URL = "https://api.nytimes.com/svc/movies/v2/reviews/search.json"
    private val API_KEY = "fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs"

    private var haveMoreReviews = false
    private var offset = 0
    private var query = ""

    private fun buildUrl(): String {
        var url = this.URL
        url = url + "?query=" + this.query
        if (this.haveMoreReviews) {
            url = url + "&offset=" + this.offset
        }
        url = url + "&api-key=" + this.API_KEY
        return url
    }

    fun loadData(): NewYorkJson.NewYorkData {

        val result =
            readUrl(buildUrl())
        val gson = Gson()
        val dataNewYork = gson.fromJson(result, NewYorkJson.NewYorkData::class.java)
        this.haveMoreReviews = dataNewYork.has_more
        if (this.haveMoreReviews) {
            this.offset += 20
        }
        val rnds = (0..5000).random()
        Thread.sleep(rnds.toLong())
        return dataNewYork
    }

    private fun readUrl(urlString: String): String {
        var reader: BufferedReader? = null
        try {
            val url = java.net.URL(urlString)
            reader = BufferedReader(InputStreamReader(url.openStream()))
            val buffer = StringBuffer()
            reader.forEachLine {
                buffer.append(it)
            }
            val bufString = buffer.toString()
            Log.d("info", bufString)
            return buffer.toString()
        } finally {
            if (reader != null)
                reader!!.close()
        }
    }

    fun setQuery(query: String) {
        this.offset = 0
        this.query = query
    }

    fun getHasMore(): Boolean{
        return this.haveMoreReviews
    }
}