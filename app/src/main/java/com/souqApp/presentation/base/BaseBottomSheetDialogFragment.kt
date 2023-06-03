package com.souqApp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.NavGraphDirections
import com.souqApp.R
import com.souqApp.presentation.dialog.GeneralDialogFragment


abstract class BaseBottomSheetDialogFragment<V : ViewBinding>(private val inflate: Inflate<V>) :
    BottomSheetDialogFragment() {

    private lateinit var _binding: V
    val binding get() = _binding

    var isLoading = false

    override fun getTheme(): Int = R.style.BottomSheet

    override fun onStart() {
        super.onStart()

        val bottomSheet = (dialog as BottomSheetDialog?)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        if(bottomSheet != null ){
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true

            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
        }

    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        isCancelable = false
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    fun showDialog(
        message: String,
        confirmText: String? = null,
        cancelTest: String? = null,
        onConfirm: () -> Unit = {},
        onCancel: () -> Unit = {}
    ) {
        navigate(
            NavGraphDirections.toBaseDialogFragment(
                message,
                confirmText ?: getString(R.string.ok),
                cancelTest
            )
        )

        setFragmentResultListener(BaseDialogFragment::class.java.simpleName) { _, bundle ->
            val action = bundle.getInt(GeneralDialogFragment.ACTION_DIALOG)
            if (action == GeneralDialogFragment.CONFIRM) {
                onConfirm()
            } else if (action == GeneralDialogFragment.CANCEL) {
                onCancel()
            }
        }
    }

    fun navigate(
        navDirections: NavDirections,
        @IdRes popUpTo: Int? = null,
        singleTop: Boolean = false
    ) {

        val navOptions = NavOptions.Builder()
            .apply {
                if (popUpTo != null)
                    setPopUpTo(popUpTo, false)
            }
            .setEnterAnim(android.R.anim.slide_in_left)
            .setLaunchSingleTop(singleTop)
            .build()

        findNavController().navigate(navDirections, navOptions)
    }

    fun showLoading(show: Boolean) {
        if (isLoading.not() && show) {
            isLoading = true
            navigate(NavGraphDirections.toLoadingDialogFragment())
        } else {
            isLoading = false
            findNavController().popBackStack()
        }

    }
}