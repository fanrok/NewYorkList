package com.example.newyorklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newyorklist.data.DatabaseHandler
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        val db = DatabaseHandler(this)
        val id = getIntent().getExtras()!!.getLong("id")
        val review = db.getReviewById(id.toInt())
        Log.d("NAME", review.Name)
        val date = findViewById<TextView>(R.id.date)
        val name = findViewById<TextView>(R.id.name)
        val text = findViewById<TextView>(R.id.detailText)
        val btn = findViewById<Button>(R.id.goBack)
//        val backImg = findViewById<ImageView>(R.id.backImg)

        if (review.Img!!.length>1) {
            val image = findViewById<ImageView>(R.id.detailImage)
            Picasso.get().load(review.Img).into(image)
        }

        date.setText(review.Date)
        name.setText(review.Name)
        text.setText(review.Text)

        btn.setOnClickListener {
            val intent = Intent(it.context, MainActivity::class.java)
            ContextCompat.startActivity(it.context, intent, null)
        }
    }

}