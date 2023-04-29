package com.souqApp.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.domain.main.home.HomeEntity
import com.souqApp.domain.products.ProductsType
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val viewModel: HomeViewModel by viewModels()
    private val snapHelperChildren = PagerSnapHelper()

    private lateinit var bestSellingAdapter: ProductGridAdapter
    private lateinit var newProductAdapter: ProductGridAdapter
    private lateinit var recommendedAdapter: ProductAdapter
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter
    private lateinit var tagAdapter: TagAdapter
    private lateinit var promotionAdapter: PromotionAdapter

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        observer()
        initListeners()
    }

    private fun navigateToProductsScreen(id: Int, name: String? = null, type: ProductsType) {
        navigate(
            NavGraphDirections.toProductsFragment(
                name.orEmpty(),
                id,
                type
            )
        )
    }

    private fun initAdapters() {
        initPromotionAdapter()
        initTagAdapter()
        initBestSellingAdapter()
        initNewProductAdapter()
        initRecommendedAdapter()
        initHomeCategoryAdapter()
    }


    private fun initBestSellingAdapter() {
        bestSellingAdapter = ProductGridAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        binding.recBestSelling.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recBestSelling.addItemDecoration(SpacesItemDecoration(20))
        binding.recBestSelling.adapter = bestSellingAdapter
    }


    private fun initNewProductAdapter() {
        newProductAdapter = ProductGridAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        binding.recNewProducts.addItemDecoration(SpacesItemDecoration(20))
        binding.recNewProducts.adapter = newProductAdapter
        binding.recNewProducts.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }


    private fun initRecommendedAdapter() {
        recommendedAdapter = ProductAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        binding.recRecommended.layoutManager = generateLinearLayoutManager()
        binding.recRecommended.adapter = recommendedAdapter
    }


    private fun initHomeCategoryAdapter() {
        homeCategoryAdapter = HomeCategoryAdapter(verticalMode = false) {
            navigateToProductsScreen(
                id = it.id,
                name = it.name,
                type = ProductsType.CATEGORY
            )
        }

        binding.recCategory.layoutManager = generateLinearLayoutManager()
        binding.recCategory.adapter = homeCategoryAdapter
    }


    private fun initTagAdapter() {
        tagAdapter = TagAdapter {
            navigateToProductsScreen(
                id = it.id,
                name = it.name,
                type = ProductsType.TAG
            )

        }

        binding.recTag.layoutManager = FlexboxLayoutManager(context)
        binding.recTag.adapter = tagAdapter
    }


    private fun initPromotionAdapter() {
        promotionAdapter = PromotionAdapter {
            navigateToProductsScreen(
                id = it,
                type = ProductsType.PROMO
            )
        }

        binding.recPromotion.layoutManager = generateLinearLayoutManager()
        binding.recPromotion.adapter = promotionAdapter
        snapHelperChildren.attachToRecyclerView(binding.recPromotion)
        binding.indicatorListPromotion.attachToRecyclerView(binding.recPromotion)
    }


    private fun navigateToProductDetails(productId: Int) {
        navigate(NavGraphDirections.toProductDetailsFragment(productId))
    }

    private fun initListeners() {
        binding.refreshSwiper.setOnRefreshListener(this)
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
            is HomeFragmentState.IsLoading -> handleIsLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
            is HomeFragmentState.HomeLoaded -> handleHomeLoaded(state.homeEntity)
            is HomeFragmentState.HomeLoadedError -> handleHomeLoadedError(state.rawResponse)
        }
    }

    private fun handleHomeLoadedError(response: WrappedResponse<HomeResponse>) {
        handleIsLoading(false)
        showDialog(response.message)
    }

    private fun handleIsLoading(loading: Boolean) {
        binding.shimmerViewContainer.isVisible = loading
        binding.refreshSwiper.isVisible = loading.not()
    }

    private fun generateLinearLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun handleHomeLoaded(homeEntity: HomeEntity) {
        //visible content
        binding.content.isVisible = true

        // set lists
        homeCategoryAdapter.list = homeEntity.categories
        bestSellingAdapter.list = homeEntity.bestSellingProducts
        recommendedAdapter.list = homeEntity.recommendedProducts
        newProductAdapter.list = homeEntity.newProducts
        promotionAdapter.list = homeEntity.promotions
        tagAdapter.list = homeEntity.tags

        //viability
        binding.cardNewProducts.isVisible = homeEntity.newProducts.isNotEmpty()
        binding.cardRecommended.isVisible = homeEntity.recommendedProducts.isNotEmpty()
        binding.cardBestSelling.isVisible = homeEntity.bestSellingProducts.isNotEmpty()
        binding.recCategory.isVisible = homeEntity.categories.isNotEmpty()
        binding.recPromotion.isVisible = homeEntity.promotions.isNotEmpty()
    }


    override fun onRefresh() {
        viewModel.getHome()
        binding.refreshSwiper.isRefreshing = false
    }

    override fun onClick(view: View) {
        when (view.id) {
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
}