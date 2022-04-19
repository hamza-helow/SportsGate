package com.souqApp.presentation.addresses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.souqApp.databinding.ActivityAddressBinding
import com.souqApp.infra.extension.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding

    private lateinit var viewModel: AddressActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )

        viewModel = ViewModelProvider(this)[AddressActivityViewModel::class.java]
        viewModel.title.observe(this, {
            supportActionBar?.title = it
        })
    }




    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}