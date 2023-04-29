package com.souqApp.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.databinding.DialogGeneralFragmentBinding
import com.souqApp.presentation.base.BaseDialogFragment

class GeneralDialogFragment :
    BaseDialogFragment<DialogGeneralFragmentBinding>(DialogGeneralFragmentBinding::inflate) {

    private val args: GeneralDialogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message = args.message
        binding.confirmText = args.confirmText
        binding.cancelText = args.cancelText
        binding.btnOk.setOnClickListener {
            findNavController().popBackStack()
            setFragmentResult(
                BaseDialogFragment::class.java.simpleName,
                bundleOf(ACTION_DIALOG to CONFIRM)
            )
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
            setFragmentResult(
                BaseDialogFragment::class.java.simpleName,
                bundleOf(ACTION_DIALOG to CANCEL)
            )
        }
    }

    companion object {

        const val ACTION_DIALOG = "base_dialog_action"
        const val CONFIRM = 1
        const val CANCEL = 2
    }
}