package com.example.newyorklist.ui.activities.entrypoint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newyorklist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryPointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_point)
    }
}