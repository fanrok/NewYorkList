package com.example.newyorklist

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    init {
        Log.d(App::class.simpleName, "App start")
    }
}