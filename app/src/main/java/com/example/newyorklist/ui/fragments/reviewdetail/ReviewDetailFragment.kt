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

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        val db = DatabaseHandler(this)
        val id = getIntent().getExtras()!!.getLong("id")
        val review = db.getReviewById(id.toInt())
        val date = findViewById<TextView>(R.id.date)
        val name = findViewById<TextView>(R.id.name)
        val text = findViewById<TextView>(R.id.detailText)
        val btn = findViewById<Button>(R.id.goBack)

        if (review.Img != null) {
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
     */
}