package com.souqApp.presentation.sub_categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.databinding.ActivitySubCategoriesBinding
import com.souqApp.domain.sub_categories.SubCategoryEntity
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.start
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SubCategoriesActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivitySubCategoriesBinding
    private lateinit var category: CategoryEntity

    private val viewModel: SubCategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        category =
            intent.getSerializableExtra(CategoryEntity::class.java.simpleName) as CategoryEntity

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )
        supportActionBar?.title = category.name

        binding.refreshSwiper.setOnRefreshListener(this)

        viewModel.getSubCategories(categoryId = category.id)

        observer()
    }

    private fun observer() {
        viewModel.mState.observe(this, { handleState(it) })
    }

    private fun handleState(state: SubCategoriesActivityState?) {
        when (state) {
            is SubCategoriesActivityState.Init -> Unit
            is SubCategoriesActivityState.CatchError -> onCatchError(state.message)
            is SubCategoriesActivityState.ErrorLoading -> Unit
            is SubCategoriesActivityState.Loaded -> onLoaded(state.subCategories)
            is SubCategoriesActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading)
    }

    private fun onCatchError(message: String) {

        Log.e("ererRR", message)

    }

    private fun onLoaded(subCategories: List<SubCategoryEntity>) {

        val subCategoriesAdapter = SubCategoriesAdapter()
        subCategoriesAdapter.list = subCategories

        binding.recSubCategory.layoutManager = GridLayoutManager(this, 2)
        binding.recSubCategory.adapter = subCategoriesAdapter
    }

    override fun onRefresh() {
        viewModel.getSubCategories(categoryId = category.id)
        binding.refreshSwiper.isRefreshing = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}