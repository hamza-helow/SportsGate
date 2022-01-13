package com.souqApp.presentation.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.domain.main.home.entity.HomeEntity
import com.souqApp.infra.extension.showLoader
import com.souqApp.infra.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.isVisible = false
        initListeners()
        observer()


    }

    private fun initListeners() {
        binding.refreshSwiper.setOnRefreshListener(this)
    }

    private fun observer() {
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: HomeFragmentState) {
        when (state) {
            is HomeFragmentState.Error -> handleShowToast(state.message)
            is HomeFragmentState.IsLoading -> handleIsLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
            is HomeFragmentState.HomeLoaded -> handleHomeLoaded(state.homeEntity)
            is HomeFragmentState.HomeLoadedError -> handleHomeLoadedError(state.rawResponse)
        }
    }

    private fun handleHomeLoadedError(rawResponse: WrappedResponse<HomeResponse>) {
        context?.showLoader(false)
    }

    private fun handleIsLoading(loading: Boolean) {
        context?.showLoader(loading)
    }

    private fun generateLinearLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun handleHomeLoaded(homeEntity: HomeEntity) {

        //visible content
        binding.content.isVisible = true
        //init adapters
        val bestSellingAdapter = ProductAdapter()
        val categoryAdapter = CategoryAdapter()
        val recommendedAdapter = ProductAdapter()
        val newProductAdapter = ProductAdapter()
        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext(), homeEntity.ads)

        // set lists
        categoryAdapter.list = homeEntity.categories
        bestSellingAdapter.list = homeEntity.bestSellingProducts
        recommendedAdapter.list = homeEntity.recommendedProducts
        newProductAdapter.list = homeEntity.newProducts

        // set layout managers
        binding.includeCategories.rec.layoutManager = generateLinearLayoutManager()
        binding.includeBestSelling.rec.layoutManager = generateLinearLayoutManager()
        binding.includeRecommended.rec.layoutManager = generateLinearLayoutManager()
        binding.includeNewProducts.rec.layoutManager = GridLayoutManager(context, 2)

        //set adapters
        binding.includeCategories.rec.adapter = categoryAdapter
        binding.includeBestSelling.rec.adapter = bestSellingAdapter
        binding.includeRecommended.rec.adapter = recommendedAdapter
        binding.includeNewProducts.rec.adapter = newProductAdapter
        binding.viewPager.adapter = sliderPagerAdapter

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        //viability
        binding.includeNewProducts.root.isVisible = homeEntity.newProducts.isNotEmpty()
        binding.includeRecommended.root.isVisible = homeEntity.recommendedProducts.isNotEmpty()
        binding.includeBestSelling.root.isVisible = homeEntity.bestSellingProducts.isNotEmpty()
        binding.includeCategories.root.isVisible = homeEntity.categories.isNotEmpty()
        binding.viewPager.isVisible = homeEntity.ads.isNotEmpty()
        binding.tabDots.isVisible = homeEntity.ads.isNotEmpty()

    }

    private fun handleShowToast(message: String) {
        context?.showToast(message)
        context?.showLoader(false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onRefresh() {
        viewModel.getHome()
        binding.refreshSwiper.isRefreshing = false
    }

}