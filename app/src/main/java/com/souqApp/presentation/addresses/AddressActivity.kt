package com.souqApp.presentation.addresses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souqApp.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}