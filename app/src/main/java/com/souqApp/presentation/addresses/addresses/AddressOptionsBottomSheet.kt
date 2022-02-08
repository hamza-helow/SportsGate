package com.souqApp.presentation.addresses.addresses

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.databinding.BottomSheetAddressOptionsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AddressOptionsBottomSheet(private val isPrimary: Boolean) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddressOptionsBinding

    lateinit var onClickDeleteButton: (() -> Unit)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetAddressOptionsBinding.inflate(inflater, container, false)
        binding.isPrimary = isPrimary
        binding.cardDelete.setOnClickListener {
            onClickDeleteButton()
            dismiss()
        }

        return binding.root
    }

}