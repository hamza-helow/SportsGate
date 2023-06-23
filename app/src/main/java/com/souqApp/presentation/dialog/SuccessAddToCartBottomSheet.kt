package com.souqApp.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.R
import com.souqApp.databinding.SuccessAddToCartBottomSheetBinding


@ExperimentalBadgeUtils
class SuccessAddToCartBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: SuccessAddToCartBottomSheetBinding

    private val args: SuccessAddToCartBottomSheetArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireActivity())
        binding = SuccessAddToCartBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.btnShowCart.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        binding.count.text = args.count.toString()

        return dialog
    }

    override fun onClick(view: View) {
        dismiss()
        when (view.id) {
            binding.btnShowCart.id -> findNavController().navigate(
                SuccessAddToCartBottomSheetDirections.toCartGraph(),
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, inclusive = true).build()
            )

            binding.btnContinue.id -> findNavController().popBackStack()
        }
    }

}