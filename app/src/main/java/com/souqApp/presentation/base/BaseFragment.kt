package com.souqApp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.souqApp.NavGraphDirections
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.utils.AppBarConfig


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<V : ViewBinding>(
    private val inflate: Inflate<V>
) : Fragment() {

    private lateinit var _binding: V
    val binding get() = _binding

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        handleBack()

        if (showAppBar())
            appBarConfig().showAppBar()
        else
            appBarConfig().hideAppBar()

        if (updateTitleBar() != null) {
            appBarConfig().updateTitleBar(updateTitleBar().orEmpty())
        }

        return binding.root
    }

    private fun handleBack() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = NavHostFragment.findNavController(this@BaseFragment)
                if (!navController.navigateUp()) {
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    fun showErrorDialog(message: String) {
        requireContext().showGenericAlertDialog(message)
    }

    fun navigate(navDirections: NavDirections, @IdRes popUpTo: Int? = null) {

        val navOptions = NavOptions.Builder()
            .apply {
                if (popUpTo != null)
                    setPopUpTo(popUpTo, false)
            }
            .setEnterAnim(android.R.anim.slide_in_left)
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