package com.souqApp.presentation.register.terms_and_condition

import android.os.Bundle
import android.view.View
import com.souqApp.databinding.FragmentTermsAndConditionsBinding
import com.souqApp.presentation.base.BaseFragment


class TermsAndConditionsFragment :
    BaseFragment<FragmentTermsAndConditionsBinding>(FragmentTermsAndConditionsBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.loadUrl("https://dellcom.advancedsouqstagingappenv.uk/t/api/v2/pages/termsAndConditions")
    }

}