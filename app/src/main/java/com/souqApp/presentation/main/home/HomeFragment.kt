package com.souqApp.presentation.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.souqApp.R
import com.souqApp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sliderPagerAdapter = SliderViewPagerAdapter(requireContext())
        binding.viewPager.adapter = sliderPagerAdapter
        binding.tabDots.setupWithViewPager(binding.viewPager)

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}