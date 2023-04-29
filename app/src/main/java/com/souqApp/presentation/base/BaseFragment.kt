package com.souqApp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.souqApp.NavGraphDirections
import com.souqApp.R
import com.souqApp.infra.utils.AppBarConfig
import com.souqApp.presentation.dialog.GeneralDialogFragment


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<V : ViewBinding>(private val inflate: Inflate<V>) : Fragment() {

    private lateinit var _binding: V
    val binding get() = _binding

    var isLoading = false

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = inflate(inflater, container, false)

        if (showAppBar())
            appBarConfig().showAppBar()
        else
            appBarConfig().hideAppBar()

        if (updateTitleBar() != null) {
            appBarConfig().updateTitleBar(updateTitleBar().orEmpty())
        }

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

    private fun appBarConfig(): AppBarConfig {
        return requireActivity() as AppBarConfig
    }

    open fun showAppBar(): Boolean = true

    open fun updateTitleBar(): String? = null

}