package com.souqApp.presentation.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.R
import com.souqApp.databinding.FragmentHomeBinding
import com.souqApp.domain.common.entity.UserEntity

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

        val productAdapter = ProductAdapter()
        val categoryAdapter = CategoryAdapter()

        productAdapter.addItem(UserEntity(1, "", "", "", "", 0, ""))
        productAdapter.addItem(UserEntity(1, "", "", "", "", 0, ""))
        productAdapter.addItem(UserEntity(1, "", "", "", "", 0, ""))

        categoryAdapter.addItem(UserEntity(1, "", "", "", "", 0, ""))
        categoryAdapter.addItem(UserEntity(1, "", "", "", "", 0, ""))

        binding.includeBestSelling.rec.adapter = productAdapter
        binding.includeBestSelling.rec.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        binding.include.rec.adapter = productAdapter
        binding.include.rec.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        binding.includeCategories.rec.adapter = categoryAdapter
        binding.includeCategories.rec.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}