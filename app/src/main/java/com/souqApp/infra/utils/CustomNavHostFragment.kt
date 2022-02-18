package com.souqApp.infra.utils


import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign

class CustomNavHostFragment : NavHostFragment() {

    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        navController.navigatorProvider += KeepStateNavigator(
            requireContext(),
            childFragmentManager,
            id
        )
    }
}