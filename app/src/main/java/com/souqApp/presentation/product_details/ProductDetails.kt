package com.souqApp.presentation.product_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.ProductDetailsResponse
import com.souqApp.databinding.ActivityProductDetailsBinding
import com.souqApp.databinding.LayoutProgressbarBinding
import com.souqApp.domain.product_details.ProductDetailsEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.setup
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.main.home.SliderViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding

    private val viewModel: ProductDetailsViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        val idProduct = intent.getIntExtra(ID_PRODUCT, 0)
        viewModel.productDetails(idProduct)

        observer()
    }

    private fun initListener() {
        binding.imgFavorite.setOnClickListener(this)
    }

    private fun init() {
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

        }
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

        handleToggleFavorite(productDetailsEntity.userFavourite)

    }

    private fun handleDetailsErrorLoaded(wrappedResponse: WrappedResponse<ProductDetailsResponse>) {

    }

    private fun handleError(throwable: Throwable) {

        Log.e("ERer", throwable.stackTraceToString())

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
            binding.imgFavorite.id -> viewModel.toggleFavorite(intent.getIntExtra(ID_PRODUCT, 0))
        }
    }

}