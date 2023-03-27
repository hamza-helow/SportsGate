package com.souqApp.presentation.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeEntity
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showLoader
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.utils.ALL_PRODUCTS
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.infra.utils.RECOMMENDED_PRODUCTS
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.ProgressDialog
import com.souqApp.presentation.products_by_type.ProductsByTypeFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var progressBar: ProgressDialog
    private lateinit var bestSellingAdapter: ProductAdapter
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter
    private lateinit var recommendedAdapter: ProductAdapter
    private lateinit var newProductAdapter: ProductGridAdapter

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.isVisible = false
        progressBar = ProgressDialog(requireContext())
        initListeners()
        initAdapters()
        observer()
    }

    private fun initAdapters() {
        bestSellingAdapter = ProductAdapter(firebaseRemoteConfig,::navigateToProductDetails)
        homeCategoryAdapter = HomeCategoryAdapter(verticalMode = false) {
            navigate(HomeFragmentDirections.toSubCategoriesGraph(it.name ?: "", it.id))
        }
        recommendedAdapter = ProductAdapter(firebaseRemoteConfig,::navigateToProductDetails)
        newProductAdapter = ProductGridAdapter(firebaseRemoteConfig, ::navigateToProductDetails)

        // set layout managers
        binding.recCategory.layoutManager = generateLinearLayoutManager()
        binding.recBestSelling.layoutManager = generateLinearLayoutManager()
        binding.recRecommended.layoutManager = generateLinearLayoutManager()
        binding.recNewProducts.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recNewProducts.addItemDecoration(SpacesItemDecoration(20))

        //set adapters
        binding.recCategory.adapter = homeCategoryAdapter
        binding.recBestSelling.adapter = bestSellingAdapter
        binding.recRecommended.adapter = recommendedAdapter
        binding.recNewProducts.adapter = newProductAdapter

    }

    private fun navigateToProductDetails(productId: Int) {
        navigate(NavGraphDirections.toProductDetailsFragment(productId))
    }

    private fun initListeners() {
        binding.refreshSwiper.setOnRefreshListener(this)
        binding.txtShowAllRecommended.setOnClickListener(this)
        binding.txtShowAllNewProducts.setOnClickListener(this)
        binding.recCategory.setOnClickListener(this)
        binding.toolbar.cardSearch.setOnClickListener(this)
        binding.toolbar.imgNotification.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.mState.observe(viewLifecycleOwner) {
            handleState(it)
        }
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

        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext(), homeEntity.promotions){
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
        binding.viewPager.adapter = sliderPagerAdapter

        // set lists
        homeCategoryAdapter.list = homeEntity.categories
        bestSellingAdapter.list = homeEntity.best_selling_products
        recommendedAdapter.list = homeEntity.recommended_products
        newProductAdapter.list = homeEntity.new_products

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        //viability
        binding.cardNewProducts.isVisible = homeEntity.new_products.isNotEmpty()
        binding.cardRecommended.isVisible = homeEntity.recommended_products.isNotEmpty()
        binding.cardBestSelling.isVisible = homeEntity.best_selling_products.isNotEmpty()
        binding.recCategory.isVisible = homeEntity.categories.isNotEmpty()
        binding.viewPager.isVisible = homeEntity.promotions.isNotEmpty()
        binding.tabDots.isVisible = homeEntity.promotions.isNotEmpty()

    }

    private fun handleUnexpectedError(throwable: Throwable) {
        context?.showLoader(false)
        binding.content.isVisible = false
        if (throwable.message != null)
            requireContext().showGenericAlertDialog(throwable.message.orEmpty())

        if (throwable is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }


    override fun onRefresh() {
        viewModel.getHome()
        binding.refreshSwiper.isRefreshing = false
    }

    private fun goToProductsByTypeScreen(type: Int) {

        val intent = Intent(
            requireContext(),
            ProductsByTypeFragment::class.java
        )
        intent.putExtra(PRODUCTS_TYPE, type)
        startActivity(intent)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.txtShowAllRecommended.id -> goToProductsByTypeScreen(RECOMMENDED_PRODUCTS)
            binding.txtShowAllNewProducts.id -> goToProductsByTypeScreen(ALL_PRODUCTS)
            binding.toolbar.cardSearch.id -> navigateToSearchFragment()
            binding.toolbar.imgNotification.id -> navigateToNotificationFragment()
        }
    }

    private fun navigateToNotificationFragment() {
        navigate(HomeFragmentDirections.toNotificationFragment())
    }

    private fun navigateToSearchFragment() {
        navigate(HomeFragmentDirections.toSearchFragment())
    }

    private fun navigateToCategoriesFragment() {
        navigate(HomeFragmentDirections.toCategoriesGraph())
    }

}