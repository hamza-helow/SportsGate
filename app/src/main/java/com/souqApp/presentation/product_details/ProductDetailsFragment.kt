package com.souqApp.presentation.product_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.databinding.FragmentProductDetailsBinding
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.main.home.SliderViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProductDetailsBinding

    private val viewModel: ProductDetailsViewModel by viewModels()

    private var idProduct = 0

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private lateinit var successAddToCartBottomSheet: SuccessAddToCartBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        init()
    }

    private fun initListener() {
        binding.imgFavorite.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
        successAddToCartBottomSheet.showCart = {
            Navigation.findNavController(requireView()).navigate(R.id.cartFragment)
        }
    }

    private fun init() {
        successAddToCartBottomSheet = SuccessAddToCartBottomSheet()

        idProduct = requireActivity().intent.getIntExtra(ID_PRODUCT, 0)
        initListener()
        binding.isLogin = sharedPrefs.isLogin()
        binding.showAddToCart = firebaseConfig.getBoolean(IS_PURCHASE_ENABLED)
        viewModel.productDetails(idProduct)

    }

    private fun observer() {
        viewModel.mState.observe(this, { handleState(it) })
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

        }
    }

    private fun handleAddingToCart(inProgress: Boolean) {
        binding.btnAddToCart.isEnabled = !inProgress

        binding.btnAddToCart.text =
            if (inProgress)
                "Adding in progress"
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

    private fun handleDetailsLoaded(productDetailsEntity: ProductDetailsEntity) {
        binding.details = productDetailsEntity

        val sliderPagerAdapter =
            SliderViewPagerAdapter(requireContext(), productDetailsEntity.images, viewOnly = true)
        binding.viewPager.adapter = sliderPagerAdapter

        val adapterRelevantProducts = AdapterRelevantProducts()
        adapterRelevantProducts.addList(productDetailsEntity.relevant)

        binding.recRelevant.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.recRelevant.adapter = adapterRelevantProducts

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        binding.cardRelevantProducts.isVisible(productDetailsEntity.relevant.isNotEmpty())

        handleToggleFavorite(productDetailsEntity.user_favourite)

    }

    private fun handleDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsEntity>) {
        requireActivity().showGenericAlertDialog(wrappedResponse.formattedErrors())
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {

    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.imgFavorite.id -> viewModel.toggleFavorite(idProduct)
            binding.btnAddToCart.id -> viewModel.addProductToCart(idProduct)
        }
    }


}