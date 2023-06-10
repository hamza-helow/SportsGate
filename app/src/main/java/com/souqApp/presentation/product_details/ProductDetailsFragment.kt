package com.souqApp.presentation.product_details

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.databinding.FragmentProductDetailsBinding
import com.souqApp.domain.product_details.AddProductToCartEntity
import com.souqApp.domain.product_details.VariationProductPriceInfoEntity
import com.souqApp.domain.products.ProductsType
import com.souqApp.infra.extension.setContent
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.activity.MainViewModel
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.TagAdapter
import com.souqApp.presentation.product_details.variations.VariationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment :
    BaseFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate),
    View.OnClickListener {

    private lateinit var tagAdapter: TagAdapter
    private lateinit var variationsAdapter: VariationsAdapter
    private lateinit var imagesProductAdapter: ImagesProductAdapter
    private lateinit var relevantProducts: AdapterRelevantProducts
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val viewModel: ProductDetailsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val snapHelper = PagerSnapHelper()

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onStart() {
        super.onStart()
        observer()
        init()
    }

    private fun initListener() {
        binding.imgFavorite.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)


    }

    override fun showAppBar(): Boolean =false

    private fun init() {
        initListener()
        binding.isLogin = sharedPrefs.isLogin()
        binding.showAddToCart = firebaseConfig.getBoolean(IS_PURCHASE_ENABLED)
        viewModel.productDetails(args.productId)

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observer() {
        viewModel.mState.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: ProductDetailsActivityState) {
        when (state) {
            is ProductDetailsActivityState.Init -> Unit
            is ProductDetailsActivityState.ToggleFavorite -> handleToggleFavorite(state.isFavorite)
            is ProductDetailsActivityState.Loading -> handleLoading(state.isLoading)
            is ProductDetailsActivityState.DetailsErrorLoaded -> handleOnError(state.wrappedResponse)
            is ProductDetailsActivityState.DetailsLoaded -> handleDetailsLoaded(state.productDetailsEntity)
            is ProductDetailsActivityState.AddedToCart -> handleAddedToCart(state.entity)
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

    private fun handleAddedToCart(entity: AddProductToCartEntity) {
        navigate(ProductDetailsFragmentDirections.toSuccessAddToCartBottomSheet(entity.productsCount))
        mainViewModel.setQty(entity.productsCount)
    }

    private fun handleToggleFavorite(favorite: Boolean) {
        binding.imgFavorite.isChecked = favorite
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun handleDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        binding.content.isVisible = true
        binding.details = productDetailsEntity
        binding.webView.setContent(productDetailsEntity.desc)

        binding.price = productDetailsEntity.price
        binding.priceAfterDiscount = productDetailsEntity.discountPrice
        binding.discount = productDetailsEntity.discountPercentage

        binding.hasRelevantProducts = productDetailsEntity.relevant.isNotEmpty()

        variationsAdapter = VariationsAdapter(
            productDetailsEntity.variations,
            productDetailsEntity.combinationOptions
        ) {
            viewModel.getVariationProductPriceInfo(productDetailsEntity.id, it)
        }

        imagesProductAdapter = ImagesProductAdapter {
            navigate(ProductDetailsFragmentDirections.toImagePreviewFragment(it))
        }

        imagesProductAdapter.list = productDetailsEntity.media

        binding.recVariations.layoutManager = LinearLayoutManager(requireContext())
        binding.recVariations.adapter = variationsAdapter

        tagAdapter = TagAdapter {
            navigate(
                NavGraphDirections.toProductsFragment(it.name, it.id, ProductsType.TAG)
            )
        }

        tagAdapter.list = productDetailsEntity.tags

        binding.recTag.layoutManager = FlexboxLayoutManager(requireContext())
        binding.recTag.adapter = tagAdapter


        relevantProducts = AdapterRelevantProducts {
            navigate(NavGraphDirections.toProductDetailsFragment(it.id))
        }

        relevantProducts.list = productDetailsEntity.relevant

        binding.recRelevant.layoutManager = GridLayoutManager(requireContext(),3)
        binding.recRelevant.adapter = relevantProducts


        binding.recPromotion.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.recPromotion.adapter = imagesProductAdapter

        snapHelper.attachToRecyclerView(binding.recPromotion)
        binding.indicatorListPromotion.attachToRecyclerView(binding.recPromotion)

        handleToggleFavorite(productDetailsEntity.isFavorite)

    }

    private fun handleOnError(wrappedResponse: WrappedResponse<*>) {
        showDialog(wrappedResponse.message)
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.imgFavorite.id -> checkIsUserLogged {
                viewModel.toggleFavorite(args.productId)
            }

            binding.btnAddToCart.id -> checkIsUserLogged {
                viewModel.addProductToCart(args.productId)
            }
        }
    }

    private fun checkIsUserLogged(success: () -> Unit) {
        if (sharedPrefs.isLogin())
            success()
        else
            navigate(ProductDetailsFragmentDirections.toAuthGraph())
    }
}
