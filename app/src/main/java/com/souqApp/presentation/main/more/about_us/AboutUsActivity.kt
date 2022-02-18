package com.souqApp.presentation.main.more.about_us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.databinding.ActivityAboutUsBinding
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.start
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding
    private val viewModel: AboutUsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup tool bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(showTitleEnabled = true)
        supportActionBar?.title = getString(R.string.about_us_str)

        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: AboutUsActivityState?) {
        when (state) {
            is AboutUsActivityState.Error -> onError(state.throwable)
            is AboutUsActivityState.ErrorLoad -> onErrorLoad(state.response)
            is AboutUsActivityState.Loaded -> onLoaded(state.contentEntity)
            is AboutUsActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        binding.txtContent.text = contentEntity.content
    }

    private fun onErrorLoad(response: WrappedResponse<ContentEntity>) {

    }

    private fun onError(throwable: Throwable) {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}