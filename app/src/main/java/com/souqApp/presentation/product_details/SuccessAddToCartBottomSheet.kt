package com.souqApp.presentation.product_details

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.R
import com.souqApp.databinding.SuccessAddToCartBottomSheetBinding

class SuccessAddToCartBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: SuccessAddToCartBottomSheetBinding

    lateinit var showCart: (() -> Unit)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireActivity())
        binding = SuccessAddToCartBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.btnShowCart.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        return dialog
    }

    override fun onClick(view: View) {
        dismiss()
        when (view.id) {
            binding.btnShowCart.id -> showCart()
        }
    }

}