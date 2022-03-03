package com.souqApp.presentation.main.more.terms_and_conditions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.databinding.ActivityTermsAndConditionsBinding
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.start
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndConditionsBinding
    private val viewModel: TermsAndConditionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setup tool bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(showTitleEnabled = true)
        supportActionBar?.title = getString(R.string.terms_and_conditions_str)

        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: TermsAndConditionsActivityState?) {

        when (state) {
            is TermsAndConditionsActivityState.Error -> onError(state.throwable)
            is TermsAndConditionsActivityState.ErrorLoad -> onErrorLoad(state.response)
            is TermsAndConditionsActivityState.Loaded -> onLoaded(state.contentEntity)
            is TermsAndConditionsActivityState.Loading -> onLoading(state.isLoading)

        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        binding.content = contentEntity.content
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