package com.souqApp.presentation.main.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.databinding.FragmentCartBinding
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val viewModel: CartFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() {

        viewModel.state.observe(viewLifecycleOwner, {
            handleState(it)
        })
    }

    private fun handleState(state: CartFragmentState?) {

        when (state) {
            is CartFragmentState.Init -> Unit
            is CartFragmentState.Error -> handleError(state.throwable)
            is CartFragmentState.CartDetailsLoaded -> handleCartDetailsLoaded(state.cartDetailsEntity)
            is CartFragmentState.CartDetailsErrorLoaded -> handleCartDetailsErrorLoaded(state.wrappedResponse)
            is CartFragmentState.Loading -> handleLoading(state.isLoading)
        }
    }

    private fun handleLoading(loading: Boolean) {

    }

    private fun handleCartDetailsErrorLoaded(wrappedResponse: WrappedResponse<CartDetailsResponse>) {

    }

    private fun handleCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {

        val cartAdapter = CartAdapter()
        cartAdapter.addList(cartDetailsEntity.products)


        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = cartAdapter

    }

    private fun handleError(throwable: Throwable) {

        Log.e("Erer", throwable.stackTraceToString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }
}