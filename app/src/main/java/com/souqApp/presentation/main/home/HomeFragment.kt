package com.souqApp.presentation.main.home

import android.content.Intent
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
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.presentation.common.CategoryAdapter
import com.souqApp.presentation.common.ProgressDialog
import com.souqApp.presentation.main.MainActivity
import com.souqApp.presentation.products_by_type.ProductsByTypeActivity
import com.souqApp.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var progressBar: ProgressDialog
    private lateinit var bestSellingAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recommendedAdapter: ProductAdapter
    private lateinit var newProductAdapter: ProductGridAdapter

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
        initAdapters()
        observer()
    }

    private fun initAdapters() {
        bestSellingAdapter = ProductAdapter()
        categoryAdapter = CategoryAdapter(verticalMode = false)
        recommendedAdapter = ProductAdapter()
        newProductAdapter = ProductGridAdapter()

        // set layout managers
        binding.recCategory.layoutManager = generateLinearLayoutManager()
        binding.recBestSelling.layoutManager = generateLinearLayoutManager()
        binding.recRecommended.layoutManager = generateLinearLayoutManager()
        binding.recNewProducts.layoutManager = GridLayoutManager(context, 3)

        //set adapters
        binding.recCategory.adapter = categoryAdapter
        binding.recBestSelling.adapter = bestSellingAdapter
        binding.recRecommended.adapter = recommendedAdapter
        binding.recNewProducts.adapter = newProductAdapter

    }

    private fun initListeners() {
        binding.refreshSwiper.setOnRefreshListener(this)
        binding.txtShowAllRecommended.setOnClickListener(this)
        binding.txtShowAllNewProducts.setOnClickListener(this)
        binding.txtShowAllCategories.setOnClickListener(this)
        binding.toolbar.cardSearch.setOnClickListener(this)
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

        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext(), homeEntity.ads)
        binding.viewPager.adapter = sliderPagerAdapter

        // set lists
        categoryAdapter.list = homeEntity.categories.toEntity()
        bestSellingAdapter.list = homeEntity.bestSellingProducts.toEntity()
        recommendedAdapter.list = homeEntity.recommendedProducts.toEntity()
        newProductAdapter.list = homeEntity.newProducts.toEntity()

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        //viability
        binding.cardNewProducts.isVisible = homeEntity.newProducts.isNotEmpty()
        binding.cardRecommended.isVisible = homeEntity.recommendedProducts.isNotEmpty()
        binding.cardBestSelling.isVisible = homeEntity.bestSellingProducts.isNotEmpty()
        binding.cardCategories.isVisible = homeEntity.categories.isNotEmpty()
        binding.viewPager.isVisible = homeEntity.ads.isNotEmpty()
        binding.tabDots.isVisible = homeEntity.ads.isNotEmpty()

    }

    private fun handleUnexpectedError(throwable: Throwable) {
        context?.showLoader(false)
        binding.content.isVisible = false

        if (throwable is SocketTimeoutException) {
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

    private fun goToProductsByTypeScreen(type: Int) {

        val intent = Intent(
            requireContext(),
            ProductsByTypeActivity::class.java
        )
        intent.putExtra(PRODUCTS_TYPE, type)
        startActivity(intent)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.txtShowAllCategories.id -> navigateToCategoriesFragment()
            binding.txtShowAllRecommended.id -> goToProductsByTypeScreen(2)
            binding.txtShowAllNewProducts.id -> goToProductsByTypeScreen(1)
            binding.toolbar.cardSearch.id -> goToSearchActivity()
        }
    }

    private fun goToSearchActivity() {
        startActivity(Intent(requireActivity(), SearchActivity::class.java))
    }

    private fun navigateToCategoriesFragment() {
        (requireActivity() as MainActivity).bottomNav.selectedItemId = R.id.categoriesFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveMainScrollState(binding.mainScroll.y)
    }

}