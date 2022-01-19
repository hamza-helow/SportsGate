package com.souqApp.presentation.main.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.main.home.remote.dto.CategoryResponse
import com.souqApp.databinding.FragmentCategoriesBinding
import com.souqApp.domain.main.home.entity.CategoryEntity
import com.souqApp.presentation.common.CategoryAdapter

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter()

        categoryAdapter.list.add(
            CategoryEntity(
                0,
                "test",
                "https://dev.2advanced.me/media/1/unnamed.png"
            )
        )

        binding.recCategory.adapter = categoryAdapter
        binding.recCategory.layoutManager = LinearLayoutManager(requireContext())


    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFragment()
    }
}