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
import com.souqApp.R
import com.souqApp.infra.utils.AppBarConfig


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<V : ViewBinding>(
    private val inflate: Inflate<V>
) : Fragment() {

    private lateinit var _binding: V
    val binding get() = _binding

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


    fun navigate(navDirections: NavDirections, @IdRes popUpTo: Int? = null) {

        val navOptions = NavOptions.Builder()
            .apply {
                if (popUpTo != null)
                    setPopUpTo(popUpTo, false)
            }
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .build()

        findNavController().navigate(navDirections, navOptions)
    }


    private fun appBarConfig(): AppBarConfig {
        return requireActivity() as AppBarConfig
    }

    open fun showAppBar(): Boolean = true

}