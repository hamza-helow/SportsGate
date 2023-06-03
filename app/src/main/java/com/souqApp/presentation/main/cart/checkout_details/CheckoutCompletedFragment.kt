package com.souqApp.presentation.main.cart.checkout_details

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.databinding.FragmentCheckoutCompletedBinding
import com.souqApp.presentation.base.BaseBottomSheetDialogFragment

class CheckoutCompletedFragment :
    BaseBottomSheetDialogFragment<FragmentCheckoutCompletedBinding>(FragmentCheckoutCompletedBinding::inflate) {

    private val args: CheckoutCompletedFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()

        binding.txtOrderDetails.setOnClickListener{
            navigate(CheckoutCompletedFragmentDirections.toOrdersGraph() ,popUpTo = R.id.homeFragment)
            navigate(CheckoutCompletedFragmentDirections.toOrderDetailsFragment(args.orderId))
        }

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment ,false)
        }
    }
}