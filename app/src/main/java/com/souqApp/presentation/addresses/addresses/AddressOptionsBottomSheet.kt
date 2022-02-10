package com.souqApp.presentation.addresses.addresses

import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.databinding.BottomSheetAddressOptionsBinding
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddressOptionsBottomSheet(private val isPrimary: Boolean) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddressOptionsBinding

    lateinit var onClickDeleteButton: (() -> Unit)

    lateinit var onClickChangeDefault: (() -> Unit)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireActivity())
        binding = BottomSheetAddressOptionsBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.isPrimary = isPrimary
        binding.cardDelete.setOnClickListener {
            onClickDeleteButton()
            dismiss()
        }

        binding.cardChangeDefault.setOnClickListener {
            onClickChangeDefault()
            dismiss()
        }

        return dialog
    }


}