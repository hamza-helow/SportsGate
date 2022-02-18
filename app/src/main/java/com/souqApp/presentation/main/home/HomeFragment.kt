package com.souqApp.presentation.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeEntity
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showLoader
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.utils.ALL_PRODUCTS
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.infra.utils.RECOMMENDED_PRODUCTS
import com.souqApp.presentation.common.CategoryAdapter
import com.souqApp.presentation.common.ProgressDialog
import com.souqApp.presentation.main.MainActivity
import com.souqApp.presentation.notification.NotificationActivity
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
        binding.toolbar.imgNotification.setOnClickListener(this)
    }

    private fun observer() {
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

    private fun handleHomeLoadedError(rawResponse: WrappedResponse<HomeEntity>) {
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

        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext(), homeEntity.products_ads)
        binding.viewPager.adapter = sliderPagerAdapter

        // set lists
        categoryAdapter.list = homeEntity.categories
        bestSellingAdapter.list = homeEntity.best_selling_products
        recommendedAdapter.list = homeEntity.recommended_products
        newProductAdapter.list = homeEntity.new_products

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        //viability
        binding.cardNewProducts.isVisible = homeEntity.new_products.isNotEmpty()
        binding.cardRecommended.isVisible = homeEntity.recommended_products.isNotEmpty()
        binding.cardBestSelling.isVisible = homeEntity.best_selling_products.isNotEmpty()
        binding.cardCategories.isVisible = homeEntity.categories.isNotEmpty()
        binding.viewPager.isVisible = homeEntity.products_ads.isNotEmpty()
        binding.tabDots.isVisible = homeEntity.products_ads.isNotEmpty()

    }

    private fun handleUnexpectedError(throwable: Throwable) {
        context?.showLoader(false)
        binding.content.isVisible = false

        Log.e("ERer", throwable.message!!)

        if (throwable.message != null)
            requireContext().showGenericAlertDialog(throwable.message!!)

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
            binding.txtShowAllRecommended.id -> goToProductsByTypeScreen(RECOMMENDED_PRODUCTS)
            binding.txtShowAllNewProducts.id -> goToProductsByTypeScreen(ALL_PRODUCTS)
            binding.toolbar.cardSearch.id -> goToSearchActivity()
            binding.toolbar.imgNotification.id -> goToNotificationActivity()
        }
    }

    private fun goToNotificationActivity() {
        startActivity(Intent(requireActivity(), NotificationActivity::class.java))
    }

    private fun goToSearchActivity() {
        startActivity(Intent(requireActivity(), SearchActivity::class.java))
    }

    private fun navigateToCategoriesFragment() {
        (requireActivity() as MainActivity).bottomNav.selectedItemId = R.id.categoriesFragment
    }

}