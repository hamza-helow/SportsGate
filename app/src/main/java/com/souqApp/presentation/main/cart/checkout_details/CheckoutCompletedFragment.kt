package com.souqApp.presentation.main.cart.checkout_details

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.databinding.FragmentCheckoutCompletedBinding
import com.souqApp.presentation.base.BaseFragment

class CheckoutCompletedFragment :
    BaseFragment<FragmentCheckoutCompletedBinding>(FragmentCheckoutCompletedBinding::inflate) {

    private val args: CheckoutCompletedFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()

        binding.txtOrderDetails.setOnClickListener{
            navigate(
                CheckoutCompletedFragmentDirections.toOrderDetailsFragment(args.orderId),
                popUpTo = R.id.homeFragment
            )
        }

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}