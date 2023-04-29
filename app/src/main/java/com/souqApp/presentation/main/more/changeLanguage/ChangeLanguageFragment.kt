package com.souqApp.presentation.main.more.changeLanguage

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.souqApp.R
import com.souqApp.databinding.FragmentChangeLanguageBinding
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangeLanguageFragment :
    BaseFragment<FragmentChangeLanguageBinding>(FragmentChangeLanguageBinding::inflate) {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCurrentLanguage()
        init()

    }

    private fun init() {
        binding.checkEnglish.setOnClickListener { setLanguage(ENGLISH) }
        binding.checkArabic.setOnClickListener { setLanguage(ARABIC) }

        binding.btnSave.setOnClickListener {
            changeLanguage()
        }
    }


    private fun setupCurrentLanguage() {
        if (Locale.getDefault() == Locale.ENGLISH) {
            setLanguage(ENGLISH)
        } else {
            setLanguage(ARABIC)
        }
    }

    private fun setLanguage(language: String) {
        binding.checkEnglish.isChecked = language == ENGLISH
        binding.checkArabic.isChecked = language == ARABIC
    }

    private fun changeLanguage() {
        sharedPrefs.setLanguage(if (binding.checkEnglish.isChecked) ENGLISH else ARABIC)
        ActivityCompat.recreate(requireActivity())
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    companion object {
        const val ENGLISH = "en"
        const val ARABIC = "ar"
    }
}