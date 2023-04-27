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
import com.souqApp.infra.extension.showLoader
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.layout_manager.CustomLinearLayoutManager
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bestSellingAdapter: ProductAdapter
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter
    private lateinit var recommendedAdapter: ProductAdapter
    private lateinit var newProductAdapter: ProductGridAdapter
    private lateinit var promotionAdapter: PromotionAdapter
    private lateinit var tagAdapter: TagAdapter
    private val snapHelperChildren = PagerSnapHelper()

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.isVisible = false
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        initAdapters()
        observer()
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
        bestSellingAdapter = ProductAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        homeCategoryAdapter = HomeCategoryAdapter(verticalMode = false) {
            navigateToProductsScreen(
                id = it.id,
                name = it.name,
                type = ProductsType.CATEGORY
            )
        }
        recommendedAdapter = ProductAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        newProductAdapter = ProductGridAdapter(firebaseRemoteConfig, ::navigateToProductDetails)
        tagAdapter = TagAdapter {
            navigateToProductsScreen(
                id = it.id,
                name = it.name,
                type = ProductsType.TAG
            )

        }
        promotionAdapter = PromotionAdapter {
            navigateToProductsScreen(
                id = it,
                type = ProductsType.PROMO
            )
        }

        // set layout managers
        binding.recPromotion.layoutManager = generateLinearLayoutManager()
        binding.recCategory.layoutManager = generateLinearLayoutManager()
        binding.recRecommended.layoutManager = CustomLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.recTag.layoutManager = FlexboxLayoutManager(context)

        val spacesItemDecoration = SpacesItemDecoration(20)

        binding.recBestSelling.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.recNewProducts.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        binding.recNewProducts.addItemDecoration(spacesItemDecoration)
        binding.recBestSelling.addItemDecoration(spacesItemDecoration)

        //set adapters
        binding.recCategory.adapter = homeCategoryAdapter
        binding.recBestSelling.adapter = bestSellingAdapter
        binding.recRecommended.adapter = recommendedAdapter
        binding.recNewProducts.adapter = newProductAdapter
        binding.recPromotion.adapter = promotionAdapter
        binding.recTag.adapter = tagAdapter


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
            is HomeFragmentState.Error -> handleUnexpectedError(state.error)
            is HomeFragmentState.IsLoading -> handleIsLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
            is HomeFragmentState.HomeLoaded -> handleHomeLoaded(state.homeEntity)
            is HomeFragmentState.HomeLoadedError -> handleHomeLoadedError(state.rawResponse)
        }
    }

    private fun handleHomeLoadedError(response: WrappedResponse<HomeResponse>) {
        handleIsLoading(false)
        showErrorDialog(response.message)
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

    private fun handleUnexpectedError(throwable: Throwable) {
        context?.showLoader(false)
        binding.content.isVisible = false
        if (throwable.message != null)
            showErrorDialog(throwable.message.orEmpty())
        if (throwable is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
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