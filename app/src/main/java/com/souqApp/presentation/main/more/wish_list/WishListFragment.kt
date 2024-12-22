package com.souqApp.presentation.main.more.wish_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentWishListBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.custome_view.flex_recycler_view.showEmptyState
import com.souqApp.infra.utils.SwipeToDeleteCallback
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishListFragment : BaseFragment<FragmentWishListBinding>(FragmentWishListBinding::inflate) {
    private val viewModel: WishListViewModel by viewModels()
    private val adapter: WishListProductAdapter by lazy {
        WishListProductAdapter {
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToWishList()
    }

    private fun observeToWishList() {

        viewModel.wishListLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorLoad(result.error)
                is BaseResult.Success -> onLoaded(result.data)
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::onLoading)
    }

    private fun onLoading(loading: Boolean) {
        showLoading(loading)
    }

    private fun onLoaded(products: List<ProductEntity>) {
        adapter.replaceList(products)

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recProducts.recyclerView)


        binding.recProducts.setAdapter(adapter, LinearLayoutManager(requireContext()))
        binding.recProducts.showEmptyState(products.isEmpty())
    }

    private fun onErrorLoad(response: WrappedListResponse<ProductEntity>) {
        showDialog(response.message)
    }

}