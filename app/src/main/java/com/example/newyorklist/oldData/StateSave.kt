package com.example.newyorklist.oldData

object StateSave {
    var reviews = mutableListOf<Review>()
    var api = LoadDataFromUrl()
    var searchText : String = ""
    var recyclerViewPosision : Int = 0
    var scrollPosition : Int = 0
}