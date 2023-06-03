package com.souqApp.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.souqApp.databinding.DialogForceUpdateFragmentBinding
import com.souqApp.presentation.base.BaseDialogFragment

class ForceUpdateDialogFragment : BaseDialogFragment<DialogForceUpdateFragmentBinding>(DialogForceUpdateFragmentBinding::inflate) {

    private val args: ForceUpdateDialogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message = args.message
        isCancelable = false
    }

}