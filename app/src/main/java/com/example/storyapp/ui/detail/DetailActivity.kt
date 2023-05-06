package com.example.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.api.Story
import com.example.storyapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]


        val userId = intent.getStringExtra(EXTRA_ID)

        if(userId != null) {
            detailViewModel.showDetailUser(this, userId)
        }

        detailViewModel.loading.observe(this) {
            showLoading(it)
        }

        detailViewModel.dataStory.observe(this) {response ->
             showDetailData(response)
        }
    }


    private fun showDetailData(response: Story) {
        Glide.with(this@DetailActivity)
            .load(response.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = response.name
        binding.tvDetailDescription.text = response.description
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.detailPage.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.detailPage.visibility = View.VISIBLE
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val TAG = "DetailActivity"
    }
}