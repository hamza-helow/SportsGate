package com.souqApp.presentation.main.home

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.souqApp.data.main.home.remote.dto.ProductAdsResponse
import com.souqApp.databinding.ItemSliderBinding
import com.souqApp.infra.utils.ID_PRODUCT
import com.souqApp.presentation.product_details.ProductDetailsActivity

class SliderViewPagerAdapter(
    val context: Context,
    private val ads: List<ProductAdsResponse>,
    private val viewOnly: Boolean = false
) :
    PagerAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return ads.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ItemSliderBinding = ItemSliderBinding.inflate(inflater, container, false)
        binding.setImage(ads[position].image)
        container.addView(binding.root)

        if (!viewOnly)
            binding.root.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(ID_PRODUCT, ads[position].id)
                context.startActivity(intent)
            }

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}