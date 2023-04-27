package com.souqApp.presentation.main.category

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.NavGraphDirections
import com.souqApp.databinding.FragmentCategoriesBinding
import com.souqApp.domain.products.ProductsType
import com.souqApp.presentation.base.BaseFragment

class CategoryChildrenFragment :
    BaseFragment<FragmentCategoriesBinding>(FragmentCategoriesBinding::inflate) {

    private val args: CategoryChildrenFragmentArgs by navArgs()

    private val adapterCategory by lazy {
        CategoryAdapter {
            navigate(NavGraphDirections.toProductsFragment(it.name ?: "", it.id , ProductsType.PROMO))
        }.apply {
            list = args.categories.toList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.recCategory.adapter = adapterCategory
    }

    override fun updateTitleBar() = args.name
}