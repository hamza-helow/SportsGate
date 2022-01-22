package com.souqApp.presentation.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.R
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.domain.main.home.entity.HomeEntity
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showLoader
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.common.CategoryAdapter
import com.souqApp.presentation.common.ProgressDialog
import com.souqApp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var progressBar: ProgressDialog

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
        progressBar = ProgressDialog(requireContext())
        initListeners()
        observer()
    }

    private fun initListeners() {
        binding.refreshSwiper.setOnRefreshListener(this)
        binding.includeCategories.txtShowAll.setOnClickListener(this)
    }

    private fun observer() {

        //get last fragment state
        viewModel.mBundleFromFragment.observe(viewLifecycleOwner, Observer {
            binding.mainScroll.y = it.getFloat("SCROLL_POSITION", 0f)
        })

        viewModel.mState.observe(viewLifecycleOwner, Observer {
            handleState(it)
        })
    }

    private fun handleState(state: HomeFragmentState) {
        when (state) {
            is HomeFragmentState.Error -> handleUnexpectedError(state.error)
            is HomeFragmentState.IsLoading -> handleIsLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
            is HomeFragmentState.HomeLoaded -> handleHomeLoaded(state.homeEntity)
            is HomeFragmentState.HomeLoadedError -> handleHomeLoadedError(state.rawResponse)
        }
    }

    private fun handleHomeLoadedError(rawResponse: WrappedResponse<HomeResponse>) {
        progressBar.showLoader(false)

        requireContext().showGenericAlertDialog("err")
        requireContext().showGenericAlertDialog(rawResponse.formattedErrors())
    }


    private fun handleIsLoading(loading: Boolean) {
        progressBar.showLoader(loading)
    }

    private fun generateLinearLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun handleHomeLoaded(homeEntity: HomeEntity) {
        //visible content
        binding.content.isVisible = true

        //init adapters
        val bestSellingAdapter = ProductAdapter()
        val categoryAdapter = CategoryAdapter(verticalMode = false)
        val recommendedAdapter = ProductAdapter()
        val newProductAdapter = ProductGridAdapter()
        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext(), homeEntity.ads)

        // set lists
        categoryAdapter.list = homeEntity.categories.toEntity()
        bestSellingAdapter.list = homeEntity.bestSellingProducts.toEntity()
        recommendedAdapter.list = homeEntity.recommendedProducts.toEntity()
        newProductAdapter.list = homeEntity.newProducts.toEntity()

        // set layout managers
        binding.includeCategories.rec.layoutManager = generateLinearLayoutManager()
        binding.includeBestSelling.rec.layoutManager = generateLinearLayoutManager()
        binding.includeRecommended.rec.layoutManager = generateLinearLayoutManager()
        binding.includeNewProducts.rec.layoutManager = GridLayoutManager(context, 3)

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

    private fun handleUnexpectedError(error: Throwable) {
        context?.showLoader(false)

        if (error is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onRefresh() {
        viewModel.getHome()
        binding.refreshSwiper.isRefreshing = false
    }

    override fun onClick(view: View) {

        when (view.id) {
            binding.includeCategories.txtShowAll.id -> navigateToCategoriesFragment()
        }
    }

    private fun navigateToCategoriesFragment() {
        (requireActivity() as MainActivity).bottomNav.selectedItemId = R.id.categoriesFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveMainScrollState(binding.mainScroll.y)
    }

}