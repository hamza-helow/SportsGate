package com.souqApp.presentation.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsEntity
import com.souqApp.databinding.ActivityProductDetailsBinding
import com.souqApp.databinding.LayoutProgressbarBinding
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.main.home.SliderViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding

    private val viewModel: ProductDetailsViewModel by viewModels()

    private var idProduct = 0

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        idProduct = intent.getIntExtra(ID_PRODUCT, 0)
        viewModel.productDetails(idProduct)

        observer()

    }

    private fun initListener() {
        binding.imgFavorite.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
    }

    private fun init() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        initListener()
        setContentView(binding.root)
        binding.isLogin = sharedPrefs.isLogin()

    }

    private fun observer() {
        viewModel.mState.observe(this, { handleState(it) })
    }

    private fun handleState(state: ProductDetailsActivityState?) {

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

        if (added)
            showToast("Added successfully")
        else
            showToast("Add failed,try later")

    }

    private fun handleToggleFavorite(favorite: Boolean) {

        binding.imgFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                this,
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
            SliderViewPagerAdapter(this, productDetailsEntity.images, viewOnly = true)
        binding.viewPager.adapter = sliderPagerAdapter

        val adapterRelevantProducts = AdapterRelevantProducts()
        adapterRelevantProducts.addList(productDetailsEntity.relevant)

        binding.recRelevant.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        binding.recRelevant.adapter = adapterRelevantProducts

        //link viewpager with tabs layout
        binding.tabDots.setupWithViewPager(binding.viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup()

        binding.cardRelevantProducts.isVisible(productDetailsEntity.relevant.isNotEmpty())

        handleToggleFavorite(productDetailsEntity.user_favourite)

    }

    private fun handleDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsEntity>) {
        showGenericAlertDialog(wrappedResponse.formattedErrors())
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        if (loading)
            setContentView(LayoutProgressbarBinding.inflate(layoutInflater).root)
        else {
            init()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {

        when (view.id) {
            binding.imgFavorite.id -> viewModel.toggleFavorite(idProduct)
            binding.btnAddToCart.id -> viewModel.addProductToCart(idProduct)
        }
    }

}