package com.souqApp.presentation.main.more.wish_list

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentWishListBinding
import com.souqApp.infra.custome_view.flex_recycler_view.showEmptyState
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class WishListFragment : BaseFragment<FragmentWishListBinding>(FragmentWishListBinding::inflate) {
    private val viewModel: WishListViewModel by viewModels()
    private val adapter: WishListProductAdapter by lazy {
        WishListProductAdapter {
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }

    override fun onResume() {
        super.onResume()
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
        showLoading(loading)
    }

    private fun onLoaded(products: List<ProductEntity>) {
        adapter.addList(products)
        binding.recProducts.setAdapter(adapter, LinearLayoutManager(requireContext()))
        binding.recProducts.showEmptyState(products.isEmpty())
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