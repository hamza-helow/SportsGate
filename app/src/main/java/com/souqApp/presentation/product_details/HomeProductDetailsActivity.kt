package com.souqApp.presentation.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souqApp.databinding.ActivityHomeProductDetailsBinding
import com.souqApp.infra.extension.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProductDetailsBinding.inflate(layoutInflater)
      //  setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}