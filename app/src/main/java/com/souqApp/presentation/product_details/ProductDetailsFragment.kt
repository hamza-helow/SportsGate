package com.souqApp.presentation.product_details

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.databinding.FragmentProductDetailsBinding
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.product_details.variations.VariationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProductDetailsFragment :
    BaseFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate),
    View.OnClickListener {

    private lateinit var variationsAdapter: VariationsAdapter

    private val args: ProductDetailsFragmentArgs by navArgs()

    private val viewModel: ProductDetailsViewModel by viewModels()

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private lateinit var successAddToCartBottomSheet: SuccessAddToCartBottomSheet

    override fun onStart() {
        super.onStart()
        observer()
        init()
    }

    private fun initListener() {
        binding.imgFavorite.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
        successAddToCartBottomSheet.showCart = {
            navigate(ProductDetailsFragmentDirections.toCartGraph())
        }
    }

    private fun init() {
        successAddToCartBottomSheet = SuccessAddToCartBottomSheet()
        initListener()
        binding.isLogin = sharedPrefs.isLogin()
        binding.showAddToCart = firebaseConfig.getBoolean(IS_PURCHASE_ENABLED)
        viewModel.productDetails(args.productId)
    }

    private fun observer() {
        viewModel.mState.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: ProductDetailsActivityState) {
        when (state) {
            is ProductDetailsActivityState.Init -> Unit
            is ProductDetailsActivityState.ToggleFavorite -> handleToggleFavorite(state.isFavorite)
            is ProductDetailsActivityState.Loading -> handleLoading(state.isLoading)
            is ProductDetailsActivityState.Error -> handleError(state.throwable)
            is ProductDetailsActivityState.DetailsErrorLoaded -> handleDetailsErrorLoaded(state.wrappedResponse)
            is ProductDetailsActivityState.DetailsLoaded -> handleDetailsLoaded(state.productDetailsEntity)
            is ProductDetailsActivityState.AddedToCart -> handleAddedToCart(state.isAdded)
            is ProductDetailsActivityState.AddingToCart -> handleAddingToCart(state.inProgress)
            is ProductDetailsActivityState.VariationProductPriceLoaded -> handleVariationProductPriceLoaded(
                state.variationProductPriceInfoEntity
            )
        }
    }

    private fun handleVariationProductPriceLoaded(priceInfoEntity: VariationProductPriceInfoEntity) {
        binding.price = priceInfoEntity.price
        binding.priceAfterDiscount = priceInfoEntity.discountPrice
        binding.discount = priceInfoEntity.discountPercentage
    }

    private fun handleAddingToCart(inProgress: Boolean) {
        binding.btnAddToCart.isEnabled = !inProgress
        binding.btnAddToCart.text =
            if (inProgress)
                getString(R.string.adding_in_progress)
            else
                getString(R.string.add_to_cart)

    }

    private fun handleAddedToCart(added: Boolean) {
        if (added) {
            successAddToCartBottomSheet.show(parentFragmentManager, "")
        } else
            requireActivity().showToast("Add failed,try later")

    }

    private fun handleToggleFavorite(favorite: Boolean) {
        binding.imgFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                if (favorite)
                    R.drawable.ic_baseline_favorite_24
                else
                    R.drawable.ic_baseline_favorite_border_24
            )
        )
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun handleDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        binding.details = productDetailsEntity
        binding.webView.wvSetContent(productDetailsEntity.desc)
        binding.price = productDetailsEntity.price
        binding.priceAfterDiscount = productDetailsEntity.discount_price
        binding.discount = productDetailsEntity.discount_percentage

        variationsAdapter = VariationsAdapter(
            productDetailsEntity.variations,
            productDetailsEntity.combination_options
        ) {
            viewModel.getVariationProductPriceInfo(productDetailsEntity.id, it)
        }
        binding.recVariations.layoutManager = LinearLayoutManager(requireContext())
        binding.recVariations.adapter = variationsAdapter


        val sliderPagerAdapter =
            ProductImagesViewPagerAdapter(requireContext(), productDetailsEntity.media)
        binding.viewPager.adapter = sliderPagerAdapter

        val adapterRelevantProducts = AdapterRelevantProducts {
            navigate(ProductDetailsFragmentDirections.toSelf(it.id))
        }
        adapterRelevantProducts.addList(productDetailsEntity.relevant)
        binding.tabDots.setupWithViewPager(binding.viewPager)

        handleToggleFavorite(productDetailsEntity.is_favorite)

    }

    private fun handleDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsEntity>) {
        showErrorDialog(wrappedResponse.message)
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.imgFavorite.id -> viewModel.toggleFavorite(args.productId)
            binding.btnAddToCart.id -> viewModel.addProductToCart(args.productId)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.wvSetContent(content: String?) {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.settings.javaScriptEnabled = true
    this.settings.loadsImagesAutomatically = true

    this.loadDataWithBaseURL(
        null,
        "<style>img{max-width: 100%}</style>$content", "text/html", "UTF-8", null
    )
}