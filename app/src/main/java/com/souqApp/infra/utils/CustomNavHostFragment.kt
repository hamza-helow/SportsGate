package com.souqApp.infra.utils

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign

class CustomNavHostFragment : NavHostFragment() {

    @SuppressLint("RestrictedApi")
    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider += KeepStateNavigator(
            requireContext(),
            childFragmentManager,
            id
        )
    }
}