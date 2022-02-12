package com.souqApp.presentation.order_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souqApp.R
import com.souqApp.databinding.ActivityOrderDetailsBinding
import com.souqApp.infra.extension.setup

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )
        supportActionBar?.title = getString(R.string.order_details)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}