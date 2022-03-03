package com.souqApp.presentation.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.search.remote.SearchEntity
import com.souqApp.databinding.ActivitySearchBinding
import com.souqApp.infra.extension.changeStatusBarColor
import com.souqApp.presentation.common.ProductHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter = ProductHorizontalAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recProducts.layoutManager = LinearLayoutManager(this)
        binding.recProducts.adapter = searchAdapter

        binding.searchView.setOnQueryTextListener(this)

        observer()

        searchAdapter.listenerNeedLoadMore = {

            viewModel.search(binding.searchView.query.toString(), it)
        }

        changeStatusBarColor(color = R.color.tool_bar_color)

    }

    private fun observer() {
        viewModel.search("", 1)
        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: SearchActivityState) {

        when (state) {
            is SearchActivityState.Error -> handleError(state.throwable)
            is SearchActivityState.ErrorLoad -> handleErrorLoad(state.response)
            is SearchActivityState.Loaded -> handleLoaded(state.searchEntity)
            is SearchActivityState.Loading -> handleLoading(state.isLoading)
        }
    }

    private fun handleLoading(loading: Boolean) {
    }

    private fun handleLoaded(searchEntity: SearchEntity) {
        searchAdapter.list = searchEntity.products
    }

    private fun handleErrorLoad(response: WrappedResponse<SearchEntity>) {

    }

    private fun handleError(throwable: Throwable) {

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(text: String): Boolean {

        if (text.isEmpty())
            searchAdapter.clearList()
        else
            viewModel.search(text, 1)
        return true
    }
}