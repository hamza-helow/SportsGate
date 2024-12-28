package com.souqApp.presentation.splash

import android.os.Bundle
import android.view.View
import com.souqApp.R
import com.souqApp.databinding.FragmentSplashBinding
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun showAppBar(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate(SplashFragmentDirections.toHomeGraph(), R.id.splashFragment, inclusive = true)
    }
}