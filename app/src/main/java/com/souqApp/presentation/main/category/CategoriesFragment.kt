package com.souqApp.presentation.main.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.souqApp.R
import com.souqApp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private lateinit var binding:FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFragment()
    }
}