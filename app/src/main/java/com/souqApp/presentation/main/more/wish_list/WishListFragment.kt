package com.souqApp.presentation.main.more.wish_list

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentWishListBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.ProductHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class WishListFragment : BaseFragment<FragmentWishListBinding>(FragmentWishListBinding::inflate) {
    private val viewModel: WishListViewModel by viewModels()
    private val adapter: ProductHorizontalAdapter by lazy {
        ProductHorizontalAdapter{
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }


    override fun onResume() {
        super.onResume()
        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = adapter
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: WishListActivityState) {

        when (state) {
            is WishListActivityState.Error -> onError(state.throwable)
            is WishListActivityState.ErrorLoad -> onErrorLoad(state.response)
            is WishListActivityState.Loaded -> onLoaded(state.products)
            is WishListActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
        binding.recProducts.isVisible(!loading)
    }

    private fun onLoaded(products: List<ProductEntity>) {
        adapter.list = products
    }

    private fun onErrorLoad(response: WrappedListResponse<ProductEntity>) {
        showDialog(response.message)
    }

    private fun onError(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }
}